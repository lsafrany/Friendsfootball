package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.shared.serialized.HistorySer;
import hu.safi.friendsfootball.shared.serialized.List1Ser;
import hu.safi.friendsfootball.shared.serialized.MatchSer;
import hu.safi.friendsfootball.shared.serialized.ParticipantSer;
import hu.safi.friendsfootball.shared.serialized.PlaceSer;
import hu.safi.friendsfootball.shared.serialized.PlayerSer;
import hu.safi.friendsfootball.shared.serialized.UserResultSer;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface FriendsfootballService extends RemoteService {

	UserResultSer user(String user, String password,String agent) throws IllegalArgumentException;
	
	void password(String key,String password) throws IllegalArgumentException;

	void sendPassword(String user) throws IllegalArgumentException;
	
	List<PlaceSer> places () throws IllegalArgumentException;
	
	PlaceSer addPlace (PlaceSer placeSer) throws IllegalArgumentException;
	
	PlaceSer updatePlace (PlaceSer placeSer) throws IllegalArgumentException;
	
	PlaceSer removePlace (PlaceSer placeSer) throws IllegalArgumentException;
		
	List<MatchSer> matchesByPlace (PlaceSer placeSer) throws IllegalArgumentException;
		
	List<MatchSer> matchesByRight (String userKey) throws IllegalArgumentException;
	
	List<MatchSer> matches (String userKey) throws IllegalArgumentException;
	
	MatchSer addMatch (MatchSer matchSer) throws IllegalArgumentException;
	
	MatchSer updateMatch (MatchSer matchSer) throws IllegalArgumentException;
	
	MatchSer updateMatchByRight (MatchSer matchSer) throws IllegalArgumentException;
	
	MatchSer removeMatch (MatchSer matchSer) throws IllegalArgumentException;
	
	List<PlayerSer> players () throws IllegalArgumentException;

	PlayerSer addPlayer (PlayerSer playerSer) throws IllegalArgumentException;
	
	PlayerSer updatePlayer (PlayerSer playerSer) throws IllegalArgumentException;
	
	PlayerSer removePlayer (PlayerSer playerSer) throws IllegalArgumentException;

	PlayerSer resetPassword (PlayerSer playerSer) throws IllegalArgumentException;
	
	List<ParticipantSer> participants (String match) throws IllegalArgumentException;
	
	ParticipantSer addParticipant(ParticipantSer participantSer) throws IllegalArgumentException;
	
	ParticipantSer updateParticipant(ParticipantSer participantSer) throws IllegalArgumentException;

	ParticipantSer updateParticipantByMoney(ParticipantSer participantSer) throws IllegalArgumentException;
	
	ParticipantSer updateParticipantByPotential(ParticipantSer participantSer) throws IllegalArgumentException;
	
	ParticipantSer removeParticipant(ParticipantSer participanSer) throws IllegalArgumentException;

	String sendMessage(String matchKey,String message) throws IllegalArgumentException;
	
	String sendBalance(String matchKey) throws IllegalArgumentException;
	
	List<HistorySer> histories () throws IllegalArgumentException;
	
	List<List1Ser> list1 () throws IllegalArgumentException;
}
