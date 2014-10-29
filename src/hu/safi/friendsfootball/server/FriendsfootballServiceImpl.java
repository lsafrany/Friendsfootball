package hu.safi.friendsfootball.server;

import hu.safi.friendsfootball.client.ClientConstants;
import hu.safi.friendsfootball.client.ClientLabels;
import hu.safi.friendsfootball.client.FriendsfootballService;
import hu.safi.friendsfootball.server.jdo.History;
import hu.safi.friendsfootball.server.jdo.Match;
import hu.safi.friendsfootball.server.jdo.PMF;
import hu.safi.friendsfootball.server.jdo.Participant;
import hu.safi.friendsfootball.server.jdo.Place;
import hu.safi.friendsfootball.server.jdo.Player;
import hu.safi.friendsfootball.shared.serialized.HistorySer;
import hu.safi.friendsfootball.shared.serialized.List1Ser;
import hu.safi.friendsfootball.shared.serialized.MatchSer;
import hu.safi.friendsfootball.shared.serialized.ParticipantSer;
import hu.safi.friendsfootball.shared.serialized.PlaceSer;
import hu.safi.friendsfootball.shared.serialized.PlayerSer;
import hu.safi.friendsfootball.shared.serialized.UserResultSer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
 @SuppressWarnings("serial")
public class FriendsfootballServiceImpl extends RemoteServiceServlet implements FriendsfootballService {
	 
	private static final Logger log = Logger.getLogger(FriendsfootballServiceImpl.class.getName()); 
	
	private static String sendMail(String toEmail, String subject, String message) {

		String[] mailRecipients = toEmail.split(";");
		
		for (int i = 0; i < mailRecipients.length; i++) {		
			Properties props = new Properties();  
			Session session = Session.getDefaultInstance(props, null);  
			try {      
				Message msg = new MimeMessage(session);  
				msg.setFrom(new InternetAddress("safi15@windowslive.com","noreply@FriendsFootballBySafi"));
				msg.addRecipient(Message.RecipientType.TO,new InternetAddress(mailRecipients[i]));
				msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "Q")); 
				msg.setText(message); 
				Transport.send(msg);				
			} catch (Exception e) {
				log.severe(e.getMessage());
				return e.getMessage();
			}
		}
		return null;
	}	
	
	private static String sendMailtoAll (String toEmail, String subject, String message) {

		String[] mailRecipients = toEmail.split(";");
		
		InternetAddress [] internetAddress = new InternetAddress [mailRecipients.length];   		
		
		for (int i = 0; i < mailRecipients.length; i++) {
			try {
				internetAddress [i] = new InternetAddress(mailRecipients[i]);
			} catch (AddressException e) {
				e.printStackTrace();
			}
		}
			
		Properties props = new Properties();  
		Session session = Session.getDefaultInstance(props, null);  
		try {      
			Message msg = new MimeMessage(session);  
			msg.setFrom(new InternetAddress("lsafrany@t-online.hu","noreply@FriendsFootball"));			
			msg.addRecipients(Message.RecipientType.TO,internetAddress);
			msg.setReplyTo(internetAddress);
			msg.setSubject(MimeUtility.encodeText(subject, "UTF-8", "Q")); 
			msg.setText(message); 
			Transport.send(msg);				
		} catch (Exception e) {
			log.severe(e.getMessage());
			return e.getMessage();
		}
		
		return null;
	}	

	public UserResultSer user(String userId,String password,String agent) throws IllegalArgumentException {
		UserResultSer userResultSer = new UserResultSer();
			
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean found = false;
		try {
			if (password == null) {
				Query query = pm.newQuery("select from " + Player.class.getName());
				query.setFilter("(id == pId)");
				query.declareParameters("String pId");
				@SuppressWarnings("unchecked")
				List<Player> list = (List<Player>) pm.newQuery(query).execute(
						userId);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Player l : list) {
						userResultSer.setKey(l.getKey().toString());
						userResultSer.setId(l.getId());
						userResultSer.setName(l.getName());
						userResultSer.setRight(l.getRight());
					}
					History history = new History(userId,"Sikeres automatikus bejelentkezés - " + agent);
					pm.makePersistent(history);						
				}	
				else  {
					userResultSer.setError(ClientLabels.LOGIN_ERROR);
					History history = new History(userId,ClientLabels.LOGIN_ERROR + " - " + agent);
					pm.makePersistent(history);
				}
			}		
			else  {
				if ((!password.equals(ClientConstants.GLOBAL_PASSWORD)) && (!password.equals(ClientConstants.INIT_PASSWORD))) {
					Query query = pm.newQuery("select from " + Player.class.getName());
					query.setFilter("(id == pId && password == pPassword)");
					query.declareParameters("String pId,String pPassword");
					@SuppressWarnings("unchecked")
					List<Player>  list = (List<Player>) pm.newQuery(query).execute(
							userId,Password.getDigestString(userId, password));
					if ((list != null) && (!list.isEmpty())) {
						found = true;
						for (Player l : list) {
							userResultSer.setKey(l.getKey().toString());
							userResultSer.setId(l.getId());
							userResultSer.setName(l.getName());
							userResultSer.setRight(l.getRight());
						}
						History history = new History(userId,"Sikeres bejelentkezés - " + agent);
						pm.makePersistent(history);
					}
					else  {
						userResultSer.setError(ClientLabels.LOGIN_ERROR);
						History history = new History(userId,ClientLabels.LOGIN_ERROR + " - " + agent);
						pm.makePersistent(history);
					}
				}
				else {		
					if (password.equals(ClientConstants.INIT_PASSWORD)) {
						Query query = pm.newQuery("select from " + Player.class.getName());
						query.setFilter("(id == pId)");
						query.declareParameters("String pId");
						@SuppressWarnings("unchecked")
						List<Player> list = (List<Player>) pm.newQuery(query).execute(
								userId);
						if ((list != null) && (!list.isEmpty())) {						
							for (Player l : list) {
								if (l.getPassword() == null) {
									found = true;								
									userResultSer.setKey(l.getKey().toString());
									userResultSer.setId(l.getId());
									userResultSer.setName(l.getName());
									userResultSer.setRight(l.getRight());																		
								}
							}	
							if (found) {
								History history = new History(userId,"Sikeres bejelentkezés alapjelszóval - " + agent);
								pm.makePersistent(history);
							}
							else  {
								userResultSer.setError(ClientLabels.LOGIN_ERROR);
								History history = new History(userId,ClientLabels.LOGIN_ERROR + " - " + agent);
								pm.makePersistent(history);
							}
						}
					}					
					if (password.equals(ClientConstants.GLOBAL_PASSWORD)) {
						Query query = pm.newQuery("select from " + Player.class.getName());
						query.setFilter("(id == pId)");
						query.declareParameters("String pId");
						@SuppressWarnings("unchecked")
						List<Player> list = (List<Player>) pm.newQuery(query).execute(
								userId);
						if ((list != null) && (!list.isEmpty())) {
							found = true;
							for (Player l : list) {
								userResultSer.setKey(l.getKey().toString());
								userResultSer.setId(l.getId());
								userResultSer.setName(l.getName());
								userResultSer.setRight(l.getRight());
							}
							History history = new History(userId,"Sikeres bejelentkezés szuperjelszóval - " + agent);
							pm.makePersistent(history);
						}
						else  {
							userResultSer.setError(ClientLabels.LOGIN_ERROR);
							History history = new History(userId,ClientLabels.LOGIN_ERROR + " - " + agent);
							pm.makePersistent(history);
						}
					}
				}	
			}
			
			if (found) {
				Query queryParticipant = pm.newQuery("select from " + Participant.class.getName());
				queryParticipant.setFilter("(player == pPlayer && right == pRight)");			
				queryParticipant.declareParameters("String pPlayer,String pRight");	
				@SuppressWarnings("unchecked")
				List<Participant> list = (List<Participant>) pm.newQuery(queryParticipant).execute(pm.getObjectById(Player.class,KeyToID(userResultSer.getKey())).getKey(),"Y");
				if ((list !=null) && (!list.isEmpty())) userResultSer.setParticipantright("Y");
				else userResultSer.setParticipantright("N");	
				log.info(userResultSer.getName() + " " + new Date());
			}
			
		} catch (Exception e) {	
			userResultSer.setError(e.getMessage());
		} finally {
			pm.close();
		}						

		return userResultSer;
	}
		
	public void password(String key,String password) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			Player player = pm.getObjectById(Player.class, KeyToID(key));
			player.setPassword(Password.getDigestString(player.getId(),password));
		} finally {
			pm.close();
		}
		
		return;
	}

	public void sendPassword(String userId) throws IllegalArgumentException {
		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			Query query = pm.newQuery("select from " + Player.class.getName());
			query.setFilter("(id == pId)");
			query.declareParameters("String pId");
			@SuppressWarnings("unchecked")
			List<Player> list = (List<Player>) pm.newQuery(query).execute(
					userId);
			if ((list !=null) && (!list.isEmpty())) {
				Random random = new Random(); 
				String password = "";
				for (Player l : list) {
					password = "ff" + random.nextInt(10000);
					l.setPassword(Password.getDigestString(l.getId(),password));
					sendMail (l.getEmail(),"Új jelszó",password);
					History history = new History(l.getId(),"Új jelszó kuldése");
					pm.makePersistent(history);
				}
			}
		} finally {
			pm.close();
		}
		
		return;
	}
	
	public ArrayList<PlaceSer> places() throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<PlaceSer> places = new ArrayList<PlaceSer>();		
		try {		
			Query query = pm.newQuery("select from " + Place.class.getName());
			@SuppressWarnings("unchecked")
			List<Place> list = (List<Place>) pm.newQuery(query).execute();
			if (!list.isEmpty()) {
				for (Place l : list) {
					PlaceSer placeSer = new PlaceSer();				
					placeSer.setKey(l.getKey().toString());				
					placeSer.setName(l.getName());
					places.add(placeSer);
				}			
			}
		} catch (Exception e) {	
			PlaceSer place = new PlaceSer();
			place.setError(e.getMessage());
			places.add(place);
		} finally {
			pm.close();
		}
					
		return places;
	}

	public PlaceSer addPlace(PlaceSer placeSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Place place = new Place(placeSer.getName());
			pm.makePersistent(place);
			placeSer.setKey(place.getKey().toString());
		} catch (Exception e) {	
			placeSer.setError(e.getMessage());
		} finally {
			pm.close();
		}

		return placeSer;
	}

	public PlaceSer updatePlace(PlaceSer placeSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Place place = pm.getObjectById(Place.class,KeyToID(placeSer.getKey()));
			place.setName(placeSer.getName());			
		} catch (Exception e) {	
			placeSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
		
		return placeSer;
	}

	public PlaceSer removePlace(PlaceSer placeSer) throws IllegalArgumentException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Place place = pm.getObjectById(Place.class,KeyToID(placeSer.getKey()));
			pm.deletePersistent(place);
		} catch (Exception e) {	
			placeSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
		
		return placeSer;
	}

	public ArrayList<MatchSer> matchesByPlace(PlaceSer placeSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<MatchSer> matches = new ArrayList<MatchSer>();
	
		try {	
			Query query = pm.newQuery("select from " + Match.class.getName());
			query.setFilter("(place == pPlace)");
			query.declareParameters("String pPlace");
			@SuppressWarnings("unchecked")
			List<Match> list = (List<Match>) pm.newQuery(query).execute(pm.getObjectById(Place.class,KeyToID(placeSer.getKey())).getKey());
			if (!list.isEmpty()) {
				for (Match l : list) {
					MatchSer matchSer = new MatchSer();				
					matchSer.setKey(l.getKey().toString());				
					matchSer.setPlace(l.getPlace().toString());
					matchSer.setDay(l.getDay());
					matchSer.setStartTime(l.getStartTime());
					matchSer.setMoney(l.getMoney());
					matches.add(matchSer);
				}			
			}
		} catch (Exception e) {	
			MatchSer matchSer = new MatchSer();
			matchSer.setError(e.getMessage());
			matches.add(matchSer);
		} finally {
			pm.close();
		}

		return matches;
	}

	public ArrayList<MatchSer> matchesByRight(String userKey) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<MatchSer> matches = new ArrayList<MatchSer>();

		try {
			Query query = pm.newQuery("select from " + Participant.class.getName());
			query.setFilter("(player == pPlayer && right == pRight)");			
			query.declareParameters("String pPlayer,String pRight");			
			@SuppressWarnings("unchecked")
			List<Participant> list = (List<Participant>) pm.newQuery(query).execute(pm.getObjectById(Player.class,KeyToID(userKey)).getKey(),"Y");
			if (!list.isEmpty()) {
				for (Participant l : list) {
					MatchSer matchSer = new MatchSer();							
					Match match = pm.getObjectById(Match.class,l.getMatch());
					matchSer.setKey(match.getKey().toString());				
					matchSer.setPlace(pm.getObjectById(Place.class,match.getPlace()).getName());
					matchSer.setDay(match.getDay());
					matchSer.setStartTime(match.getStartTime());
					matchSer.setMoney(match.getMoney());
					matches.add(matchSer);
				}			
			}
		} catch (Exception e) {	
			MatchSer matchSer = new MatchSer();
			matchSer.setError(e.getMessage());
			matches.add(matchSer);
		} finally {
			pm.close();
		}

		return matches;
	}
	
	public ArrayList<MatchSer> matches(String userKey) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<MatchSer> matches = new ArrayList<MatchSer>();

		try {
			Query query = pm.newQuery("select from " + Participant.class.getName());
			query.setFilter("(player == pPlayer)");
			query.declareParameters("String pPlayer");
			@SuppressWarnings("unchecked")
			List<Participant> list = (List<Participant>) pm.newQuery(query).execute(pm.getObjectById(Player.class,KeyToID(userKey)).getKey());
			if (!list.isEmpty()) {
				for (Participant l : list) {
					MatchSer matchSer = new MatchSer();							
					Match match = pm.getObjectById(Match.class,l.getMatch());
					matchSer.setKey(match.getKey().toString());				
					matchSer.setPlace(pm.getObjectById(Place.class,match.getPlace()).getName());
					matchSer.setDay(match.getDay());
					matchSer.setStartTime(match.getStartTime());
					matchSer.setMoney(match.getMoney());
					matches.add(matchSer);
				}			
			}
		} catch (Exception e) {	
			MatchSer matchSer = new MatchSer();
			matchSer.setError(e.getMessage());
			matches.add(matchSer);
		} finally {
			pm.close();
		}

		return matches;
	}

	public MatchSer addMatch(MatchSer matchSer) throws IllegalArgumentException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {						
			Place place = pm.getObjectById(Place.class,KeyToID(matchSer.getPlace()));
			Match match = new Match(place.getKey(),matchSer.getDay(),matchSer.getStartTime(),0);		
			pm.makePersistent(match);	
			matchSer.setKey(match.getKey().toString());
		} catch (Exception e) {	
			matchSer.setError(e.getMessage());
		} finally {
			pm.close();
		}

		return matchSer;
	}

	public MatchSer updateMatch(MatchSer matchSer) throws IllegalArgumentException {
				
		PersistenceManager pm = PMF.get().getPersistenceManager();		
		try {
			Match match = pm.getObjectById(Match.class,KeyToID(matchSer.getKey()));
			match.setDay(matchSer.getDay());
			match.setStartTime(matchSer.getStartTime());
			match.setMoney(matchSer.getMoney());
		} catch (Exception e) {	
			matchSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
								
		return matchSer;
	}

	public MatchSer updateMatchByRight(MatchSer matchSer) throws IllegalArgumentException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();		
		try {
			Match match = pm.getObjectById(Match.class,KeyToID(matchSer.getKey()));
			match.setMoney(matchSer.getMoney());
		} catch (Exception e) {	
			matchSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
								
		return matchSer;
	}

	public MatchSer removeMatch(MatchSer matchSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {	
			Match match = pm.getObjectById(Match.class,KeyToID(matchSer.getKey()));
			pm.deletePersistent(match);
		} catch (Exception e) {	
			matchSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
		
		return matchSer;
	}

	public ArrayList<PlayerSer> players() throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<PlayerSer> players = new ArrayList<PlayerSer>();
		try {	
			Query query = pm.newQuery("select from " + Player.class.getName());
			@SuppressWarnings("unchecked")
			List<Player> list = (List<Player>) pm.newQuery(query).execute();
			if (!list.isEmpty()) {
				for (Player l : list) {
					PlayerSer playerSer = new PlayerSer();				
					playerSer.setKey(l.getKey().toString());
					playerSer.setId(l.getId());
					playerSer.setName(l.getName());
					playerSer.setRight(l.getRight());
					playerSer.setEmail(l.getEmail());
					players.add(playerSer);
				}			
			}
		} catch (Exception e) {	
			PlayerSer playerSer = new PlayerSer();				
			playerSer.setError(e.getMessage());
			players.add(playerSer);
		} finally {
			pm.close();
		}

		return players;
	}
	
	public PlayerSer addPlayer(PlayerSer playerSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {							
			Query query = pm.newQuery("select from " + Player.class.getName());
			query.setFilter("(id == pId)");
			query.declareParameters("String pId");
			@SuppressWarnings("unchecked")
			List<Player>  list = (List<Player>) pm.newQuery(query).execute(playerSer.getId());
			if ((list !=null) && (!list.isEmpty())) {
				Random random = new Random();
				playerSer.setId(playerSer.getId() + random.nextInt(10000));
			}	
			Player player = new Player(playerSer.getId(),playerSer.getName(),null,playerSer.getEmail(),playerSer.getRight());
			pm.makePersistent(player);
			playerSer.setKey(player.getKey().toString());										
		} catch (Exception e) {	
			playerSer.setError(e.getMessage());
		} finally {
			pm.close();
		}

		return playerSer;
	}

	public PlayerSer updatePlayer(PlayerSer playerSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {					
			Query query = pm.newQuery("select from " + Player.class.getName());
			query.setFilter("(id == pId)");
			query.declareParameters("String pId");
			@SuppressWarnings("unchecked")
			List<Player>  list = (List<Player>) pm.newQuery(query).execute(playerSer.getId());
			boolean idOk = true; 
			if ((list !=null) && (!list.isEmpty())) {
				for (Player l : list) {
					if (!l.getKey().toString().equals(playerSer.getKey())) idOk = false;
				}											
			}
			
			if (!idOk) {
				Random random = new Random();
				playerSer.setId(playerSer.getId() + random.nextInt(10000));				
			}
			Player player = pm.getObjectById(Player.class,KeyToID(playerSer.getKey()));
			player.setId(playerSer.getId());	
			player.setName(playerSer.getName());
			player.setRight(playerSer.getRight());
			player.setEmail(playerSer.getEmail());
		} catch (Exception e) {	
			playerSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
		
		return playerSer;
	}

	public PlayerSer resetPassword(PlayerSer playerSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Player player = pm.getObjectById(Player.class,KeyToID(playerSer.getKey()));
			player.setPassword(null);
		} catch (Exception e) {	
			playerSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
		
		return playerSer;
	}
	
	public PlayerSer removePlayer(PlayerSer playerSer) throws IllegalArgumentException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Player player = pm.getObjectById(Player.class,KeyToID(playerSer.getKey()));
			pm.deletePersistent(player);
		} catch (Exception e) {	
			playerSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
		
		return playerSer;
	}
				
	public ArrayList<ParticipantSer> participants(String match) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<ParticipantSer> participants = new ArrayList<ParticipantSer>();
		try {		
			Query queryParticipant = pm.newQuery("select from " + Participant.class.getName());
			queryParticipant.setFilter("(match == pMatch)");
			queryParticipant.declareParameters("String pMatch");
			@SuppressWarnings("unchecked")
			List<Participant> list = (List<Participant>) pm.newQuery(queryParticipant).execute(pm.getObjectById(Match.class,KeyToID(match)).getKey());
			if (!list.isEmpty()) {
				for (Participant l : list) {
					ParticipantSer participantSer = new ParticipantSer();		
					Player player = pm.getObjectById(Player.class, l.getPlayer());				
					participantSer.setKey(l.getKey().toString());
					participantSer.setId(player.getId());
					participantSer.setName(player.getName());
					participantSer.setMatch(l.getMatch().toString());
					participantSer.setPlayer(l.getPlayer().toString());					
					if (l.getPotential() == null) participantSer.setPotential("");
					else {
						if (l.getPotential()) participantSer.setPotential("Y");
						else participantSer.setPotential("N");									
					}
					participantSer.setMoney(l.getMoney());			
					participantSer.setRight(l.getRight());					
					participants.add(participantSer);					
				}			
			}
		} catch (Exception e) {	
			ParticipantSer participantSer = new ParticipantSer();			
			participantSer.setError(e.getMessage());
			participants.add(participantSer);
		} finally {
			pm.close();
		}
		return participants;
	}
			
	public ParticipantSer addParticipant(ParticipantSer participantSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {			
			Participant participant = new Participant(pm.getObjectById(Match.class,KeyToID(participantSer.getMatch())).getKey(), pm.getObjectById(Player.class,KeyToID(participantSer.getPlayer())).getKey(), null , participantSer.getMoney(), participantSer.getRight());
			pm.makePersistent(participant);
			participantSer.setKey(participant.getKey().toString());							
		} catch (Exception e) {		
			participantSer.setError(e.getMessage());
		} finally {
			pm.close();
		}

		return participantSer;
	}
	
	public ParticipantSer updateParticipant(ParticipantSer participantSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			Participant participant = pm.getObjectById(Participant.class, KeyToID(participantSer.getKey()));
			if (participantSer.getPotential().equals("")) participant.setPotential(null); 
			if (participantSer.getPotential().equals("Y")) participant.setPotential(true);
			if (participantSer.getPotential().equals("N")) participant.setPotential(false); 
			participant.setMoney(participantSer.getMoney());
			participant.setRight(participantSer.getRight());
		} catch (Exception e) {		
			participantSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
				
		return participantSer;
	}

	public ParticipantSer updateParticipantByMoney(ParticipantSer participantSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			Participant participant = pm.getObjectById(Participant.class, KeyToID(participantSer.getKey()));
			participant.setMoney(participantSer.getMoney());
		} catch (Exception e) {		
			participantSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
				
		return participantSer;
	}
	
	public ParticipantSer updateParticipantByPotential(ParticipantSer participantSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			Participant participant = pm.getObjectById(Participant.class, KeyToID(participantSer.getKey()));
			if (participantSer.getPotential().equals("")) participant.setPotential(null); 
			if (participantSer.getPotential().equals("Y")) participant.setPotential(true);
			if (participantSer.getPotential().equals("N")) participant.setPotential(false); 
		} catch (Exception e) {		
			participantSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
				
		return participantSer;
	}
	
	public ParticipantSer removeParticipant(ParticipantSer participiantSer) throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {						
			Participant participant = pm.getObjectById(Participant.class,KeyToID(participiantSer.getKey()));
			pm.deletePersistent(participant);
		} catch (Exception e) {	
			participiantSer.setError(e.getMessage());
		} finally {
			pm.close();
		}
		
		return participiantSer;
	}
	

	public String sendMessage(String matchKey,String message) throws IllegalArgumentException {
		String result = null;
		String emails = "";
		String day = "";
		String subject =  "";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Match match = pm.getObjectById(Match.class,KeyToID(matchKey));			
			if (match.getDay().equals("1")) day = ClientLabels.DAY1;
			else if (match.getDay().equals("2")) day = ClientLabels.DAY2;
			else if (match.getDay().equals("3")) day = ClientLabels.DAY3;
			else if (match.getDay().equals("4")) day = ClientLabels.DAY4;
			else if (match.getDay().equals("5")) day = ClientLabels.DAY5;
			else if (match.getDay().equals("6")) day = ClientLabels.DAY6;
			else if (match.getDay().equals("7")) day = ClientLabels.DAY7;						
			subject = pm.getObjectById(Place.class,match.getPlace()).getName() + " " + day + " " + match.getStartTime() + " üzenet";

			Query queryParticipant = pm.newQuery("select from " + Participant.class.getName());
			queryParticipant.setFilter("(match == pMatch)");
			queryParticipant.declareParameters("String pMatch");
			@SuppressWarnings("unchecked")
			List<Participant> list = (List<Participant>) pm.newQuery(queryParticipant).execute(pm.getObjectById(Match.class,KeyToID(matchKey)).getKey());
			if (!list.isEmpty()) {
				for (Participant l : list) {
					Player player = pm.getObjectById(Player.class, l.getPlayer());
					if ((player.getEmail() != null) &&  (player.getEmail().length() > 1)) emails = emails + player.getEmail() + ";";					
				}			
			}
		} catch (Exception e) {	
			result = e.getMessage();
		} finally {
			pm.close();
		}
		
		result = sendMailtoAll (emails,subject,message);		
		return result;
	}

	public String sendBalance(String matchKey) throws IllegalArgumentException {
		String result = null;
		String emails = "";
		String message = "";
		String day = "";
		String subject =  "";
		Integer sum = new Integer (0);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Match match = pm.getObjectById(Match.class,KeyToID(matchKey));			
			if (match.getDay().equals("1")) day = ClientLabels.DAY1;
			else if (match.getDay().equals("2")) day = ClientLabels.DAY2;
			else if (match.getDay().equals("3")) day = ClientLabels.DAY3;
			else if (match.getDay().equals("4")) day = ClientLabels.DAY4;
			else if (match.getDay().equals("5")) day = ClientLabels.DAY5;
			else if (match.getDay().equals("6")) day = ClientLabels.DAY6;
			else if (match.getDay().equals("7")) day = ClientLabels.DAY7;	
			subject = pm.getObjectById(Place.class,match.getPlace()).getName() + " " + day + " " + match.getStartTime() + " egyenleg";					
			Query queryParticipant = pm.newQuery("select from " + Participant.class.getName());
			queryParticipant.setFilter("(match == pMatch)");
			queryParticipant.declareParameters("String pMatch");
			@SuppressWarnings("unchecked")
			List<Participant> list = (List<Participant>) pm.newQuery(queryParticipant).execute(pm.getObjectById(Match.class,KeyToID(matchKey)).getKey());
			if (!list.isEmpty()) {
				for (Participant l : list) {
					Player player = pm.getObjectById(Player.class, l.getPlayer());
					if ((player.getEmail() != null) &&  (player.getEmail().length() > 1)) emails = emails + player.getEmail() + ";";					
					message = message + (player.getName() + " : " + l.getMoney() + "\n");
					sum = sum +  l.getMoney();
				}			
			}
			message = message + "-------------------------------------------------\n"; 
			message = message + "Összesen : " + sum + "\n"; 
			message = message + "=================================================\n"; 
			message = message + "Díj : " + match.getMoney() + "\n"; 

		} catch (Exception e) {	
			result = e.getMessage();
		} finally {
			pm.close();
		}
		
		result = sendMailtoAll (emails,subject,message);				
		return result;
	}
	
	public ArrayList<HistorySer> histories() throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<HistorySer> histories = new ArrayList<HistorySer>();		
		try {		
			Query query = pm.newQuery("select from " + History.class.getName());
			query.setOrdering("date desc");			 
			@SuppressWarnings("unchecked")
			List<History> list = (List<History>) pm.newQuery(query).execute();
			if (!list.isEmpty()) {
				for (History l : list) {
					HistorySer historySer = new HistorySer();				
					historySer.setKey(l.getKey().toString());	
					historySer.setUserId(l.getUserId());	
					historySer.setMessage(l.getMessage());
					historySer.setDate(l.getDate());	
					histories.add(historySer);
				}			
			}
		} catch (Exception e) {	
			HistorySer historySer = new HistorySer();	
			historySer.setError(e.getMessage());
			histories.add(historySer);
		} finally {
			pm.close();
		}
					
		return histories;
	}
	
	public ArrayList<List1Ser> list1() throws IllegalArgumentException {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<List1Ser> list1 = new ArrayList<List1Ser>();		
		try {		
			String day = "";
			Query query = pm.newQuery("select from " + Player.class.getName());
			query.setOrdering("id asc");
			@SuppressWarnings("unchecked")
			List<Player> list = (List<Player>) pm.newQuery(query).execute();
			if (!list.isEmpty()) {
				for (Player l : list) {														
					Query subQuery = pm.newQuery("select from " + Participant.class.getName());
					subQuery.setFilter("(player == pPlayer)");
					subQuery.declareParameters("String pPlayer");
					@SuppressWarnings("unchecked")
					List<Participant> subList = (List<Participant>) pm.newQuery(subQuery).execute(l.getKey());
					if (!subList.isEmpty()) {
						for (Participant sl : subList) {
							List1Ser list1Ser = new List1Ser();
							list1Ser.setUserId(l.getId());	
							list1Ser.setName(l.getName());					
							Match match = pm.getObjectById(Match.class,sl.getMatch());
						
							if (match.getDay().equals("1")) day = ClientLabels.DAY1;
							else if (match.getDay().equals("2")) day = ClientLabels.DAY2;
							else if (match.getDay().equals("3")) day = ClientLabels.DAY3;
							else if (match.getDay().equals("4")) day = ClientLabels.DAY4;
							else if (match.getDay().equals("5")) day = ClientLabels.DAY5;
							else if (match.getDay().equals("6")) day = ClientLabels.DAY6;
							else if (match.getDay().equals("7")) day = ClientLabels.DAY7;						
						
							list1Ser.setMatch(pm.getObjectById(Place.class,match.getPlace()).getName() + " " + day + " " + match.getStartTime());
							list1.add(list1Ser);
						}			
					}
					else { 
						List1Ser list1Ser = new List1Ser();
						list1Ser.setUserId(l.getId());	
						list1Ser.setName(l.getName());
						list1Ser.setMatch("");	
						list1.add(list1Ser);
					}
					
				}			
			}
		} catch (Exception e) {	
			List1Ser list1Ser = new List1Ser();	
			list1Ser.setError(e.getMessage());
			list1.add(list1Ser);
		} finally {
			pm.close();
		}
					
		return list1;
	}
	
	private long KeyToID (String key) {
		key = key.substring(key.indexOf("(") + 1, key.indexOf(")"));
		return Long.valueOf(key).longValue();
	}	
	
}
