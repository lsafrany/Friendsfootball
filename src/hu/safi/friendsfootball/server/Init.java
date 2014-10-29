
// http://127.0.0.1:8888/friendsfootball/init

package hu.safi.friendsfootball.server;

import hu.safi.friendsfootball.server.jdo.History;
import hu.safi.friendsfootball.server.jdo.Match;
import hu.safi.friendsfootball.server.jdo.PMF;
import hu.safi.friendsfootball.server.jdo.Participant;
import hu.safi.friendsfootball.server.jdo.Place;
import hu.safi.friendsfootball.server.jdo.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Init extends HttpServlet {
		
	private static final Logger log = Logger.getLogger(Init.class.getName()); 
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Date() + " start.");		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<html>");
		out.append("<head>");
		out.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		out.append("<meta http-equiv=\"expires\" content=\"Mon, 22 Jul 2002 11:12:01 GMT\">");
		out.append("</head>");
		out.append("<body>");
				
		PersistenceManager pm = PMF.get().getPersistenceManager();		
		try {

			Query histories = pm.newQuery(History.class);
			histories.deletePersistentAll();

			Query places = pm.newQuery(Place.class);
			places.deletePersistentAll();

			Place place1 = new Place("Tanuló utca");

			pm.makePersistent(place1);
			
			Query players = pm.newQuery(Player.class);
			players.deletePersistentAll();

/*		
			"","valentin","Player(24002)","Tobias Valentin","N",
			"rez.mihaly@t-online.hu","rez_2","Player(24003)","Ifj. Réz Mihály",
			"somogyizoltan21@freemail.hu","zoli","Player(25002)","Somogyi Zoltán",
			"plusgabi@freemail.hu","lorincz_1","Player(25003)","Lőrincz Gábor",
			"laszlo.safrany@dwpbank.de;lsafrany@t-online.hu","safi","Player(26001)","Sáfrány László","Y",
			"lorincz_2","Player(26002)","Lőrincz Árpád",
			"pal.villam@t-online.hu","villam_1","Player(27001)","Villám Pál",
			"axelfoeley@freemail.hu","feri","Player(27002)","Szabó Ferenc",
			"kelemennet@yahoo.com","marci","Player(27003)","Kelemen Márton",
			"szekely_1","Player(27004)","Székely Mihály",
			"istvan.hosszu@t-email.hu","szuka","Player(28001)","Hosszú István",
			"rez.mihaly@t-email.hu","rez_1","Player(28002)","Réz Mihály",
			"gergelylaszlo@freemail.hu","gergely","Player(29001)","Gergely László",
			"koco","Player(29002)","Koczó László",
			"szekely7@gmail.com","szekely_2","Player(30001)","Székely András",
			"lovasar@gmail.com","barno","Player(33003)","Barnóczki Lászó",
			"juhasz.laszlo.64@freemail.hu","juhasz","Player(33006)","Juhász László",
			"szekelyd15@gmail.com","szekely_5","Player(34014)","Székely Domokos",
			"bodiat69@gmail.com","bodi","Player(35009)","Bódi Attila",
			"szekelyagoston@gmail.com","szekely_4","Player(35010)","Székely Ágoston",
			"szepvolgyi.tibor@t-online.hu","szepi","Player(35015)","Szépvölgyi Tibor",
			"matuska99@gmail.com","szekely_3","Player(37007)","Székely Mátyás",
			"pal.varga@bdi.hu","vargapali","Player(39001)","Varga Pál",
			"dave.bors@hotmail.com","villam_2","Player(52002)","Bors Dávid",
			"keller_richard_janos@hotmail.com","keller_1","Player(63001)","Keller Richárd János",
			"tepi@chello.hu","tepi","Player(81001)","Teplán Krisztián",
			"csonti28@gmail.com","csonti_2","Player(199007)","Csontos Bencze",
			"somogyit@gmail.com","soma","Player(201005)","Somogyi Tibor",
			"csonti_1","Player(228004)","Csontos Lászó",
			"nagyboldi@ludens.elte.hu","bnagy_1","Player(606002)","Nagy Boldizsár",
			"friedrich_mark@student.ceu.hu","fmark","Player(606003)","Friedrich Mark",
			"jenonizak@gmail.com","nizak_2","Player(606004)","Nizak Jeno",
			"toporczy@gmail.com","toporczy","Player(608002)","Toporczy Ádám",
			"lattmann@ajk.elte.hu","tlattmann","Player(609004)","Lattmann Tamás",
			"kalman.mizsei@opensocietyfoundation.org","kmizsei","Player(610002)",
			"Kalman Mizsei","matkatona@gmail.com","mkatona","Player(612003)","Katona Mate",
			"nabe33@gmail.com","bnagy_2","Player(613002)","Nagy Benedek",
			"zeljko.jovanovic@opensocietyfoundations.org","zjovanovic","Player(616001)",
			"Zeljko Jovanovic","adem.ademi@opensocietyfoundations.org","aademi","Player(616002)","Adem Ademi",
			"peter.nizak@opensocietyfoundations.org","nizak_1","Player(617001)","Nizak Péter",
			"tomlo","Player(3569002)","Kármán László",
			"gabori","Player(3569003)","Gábor István","kulcsar","Player(3629003)","Kulcsár Zsolt",
			"urbanbalu@gmail.com","urbanb","Player(3639005)","Urbán Balázs",
			"fpisti89@gmail.com","fpisti","Player(3679002)","Fodor István",
			"peter.kapolnasi@gmail.com","kapolp","Player(3679003)","Kápolnási Péter",
			"molnarm","Player(3679005)","Molnár Menyhért",
			"foka2004@gmail.com","gallai","Player(3689002)","Gallai György",
			"csizmadia.tamas@citromail.hu","csizi","Player(3699001)","Csizmadia Tamás",
			"pikozs","Player(3709003)","Pikó Zsolt",
			"maroz","Player(3709004)","Maró Zalán",
			"petomes@googlemail.com","petozs","Player(3729001)","Pető Zsolt",
			"pintacsit","Player(3739004)","Pintácsi Tamás",
			"nagynorbert46@gmail.com","macu","Player(3749001)","Nagy Norbert",
			"urband","Player(3829001)","Urbán Dávid",
			"urbanp","Player(3849001)","Urbán Péter",
			"foka","Player(3939004)",
			"szecsi.laszlo@freemail.hu","szecsil","Player(4029002)","Szécsi László",
*/			
						
			Player player1 = new Player("safi","Sáfrány László",null,"laszlo.safrany@dwpbank.de;lsafrany@t-online.hu","Y");
			Player player2 = new Player("pali_1","Villám Pál",null,"pal.villam@t-online.hu","N");
			Player player3 = new Player("szekely_1","Székely Mihály",null,"","N");
			Player player4 = new Player("szekely_2","Székely András",null,"szekely7@gmail.com","N");
			Player player5 = new Player("szekely_3","Székely Mátyás",null,"matuska99@gmail.com","N");
			Player player6 = new Player("szekely_4","Székely Ágoston",null,"szekelyagoston@gmail.com","N");			
			Player player7 = new Player("barno","Barnóczki László",null,"lovasar@gmail.com","Y");
			Player player8 = new Player("juhasz","Juhász László",null,"juhasz.laszlo.64@freemail.hu","N");
			Player player9 = new Player("vargapali","Varga Pál",null,"pal.varga@bdi.hu","N");
				
			pm.makePersistent(player1);
			pm.makePersistent(player2);
			pm.makePersistent(player3);
			pm.makePersistent(player4);
			pm.makePersistent(player5);
			pm.makePersistent(player6);
			pm.makePersistent(player7);
			pm.makePersistent(player8);
			pm.makePersistent(player9);
			
			Query matches = pm.newQuery(Match.class);
			matches.deletePersistentAll();
									
			Query queryPlace = pm.newQuery("select from " + Place.class.getName());
			@SuppressWarnings("unchecked")
			List<Place> placeList = (List<Place>) pm.newQuery(queryPlace).execute();
			if (!placeList.isEmpty()) {
				for (Place p : placeList) {
					if (p.getName().equals("Tanuló utca")) {
						Match match = new Match(p.getKey(),"4","18:00",0);					
						pm.makePersistent(match);						
					}
				}
			}

			Query participants = pm.newQuery(Participant.class);
			participants.deletePersistentAll();

			Query queryMatch1 = pm.newQuery("select from " + Match.class.getName());
			@SuppressWarnings("unchecked")
			List<Match> matchList1 = (List<Match>) pm.newQuery(queryMatch1).execute();
			if (!matchList1.isEmpty()) {

				for (Match m : matchList1) {
					
					if (pm.getObjectById(Place.class, m.getPlace()).getName().equals("Tanuló utca")) {
						
						Participant participant1 = new Participant(m.getKey(), player1.getKey(), Boolean.FALSE,0,"N");
						Participant participant2 = new Participant(m.getKey(), player2.getKey(), Boolean.FALSE,0,"N");
						Participant participant3 = new Participant(m.getKey(), player3.getKey(), Boolean.FALSE,0,"N");
						Participant participant4 = new Participant(m.getKey(), player4.getKey(), Boolean.FALSE,0,"N");
						Participant participant5 = new Participant(m.getKey(), player5.getKey(), Boolean.FALSE,0,"N");
						Participant participant6 = new Participant(m.getKey(), player6.getKey(), Boolean.FALSE,0,"N");
						Participant participant7 = new Participant(m.getKey(), player7.getKey(), Boolean.FALSE,0,"N");
						Participant participant8 = new Participant(m.getKey(), player8.getKey(), Boolean.FALSE,0,"N");
						Participant participant9 = new Participant(m.getKey(), player9.getKey(), Boolean.FALSE,0,"N");

						pm.makePersistent(participant1);
						pm.makePersistent(participant2);
						pm.makePersistent(participant3);
						pm.makePersistent(participant4);
						pm.makePersistent(participant5);
						pm.makePersistent(participant6);	
						pm.makePersistent(participant7);
						pm.makePersistent(participant8);
						pm.makePersistent(participant9);
					}
				}
			}

			Query queryMatch2 = pm.newQuery("select from " + Match.class.getName());
			@SuppressWarnings("unchecked")
			List<Match> matchList2 = (List<Match>) pm.newQuery(queryMatch2).execute();
			if (!matchList2.isEmpty()) {			

				for (Match m : matchList2) {
					out.append("<h1>" + pm.getObjectById(Place.class, m.getPlace()).getName() + " - "
							+ m.getDay() + " - " + m.getStartTime() + "</h1>");

					Query queryParticipant = pm.newQuery("select from " + Participant.class.getName());
					queryParticipant.setFilter("(match == pMatch)");
					queryParticipant.declareParameters("String pMatch");
					@SuppressWarnings("unchecked")
					List<Participant> participantList = (List<Participant>) pm.newQuery(queryParticipant).execute(m.getKey());
					if (!participantList.isEmpty()) {
						out.append("<table>");
						for (Participant p : participantList) {
							out.append("<tr><td>" + pm.getObjectById(Player.class, p.getPlayer()).getName() + " - " + p.getRight() + "</td></tr>" );
						}
						out.append("</table>");
					}				
				}
			}

			History history = new History ("admin","Init");
			pm.makePersistent(history);
			
		} finally {
			pm.close();
		}
		
		out.append("</body>");
		out.append("</html>");
		
		out.println();
		log.info(new Date() + " stop.");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
