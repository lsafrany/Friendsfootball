package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.shared.serialized.HistorySer;
import hu.safi.friendsfootball.shared.serialized.List1Ser;
import hu.safi.friendsfootball.shared.serialized.MatchSer;
import hu.safi.friendsfootball.shared.serialized.ParticipantSer;
import hu.safi.friendsfootball.shared.serialized.PlaceSer;
import hu.safi.friendsfootball.shared.serialized.PlayerSer;
import hu.safi.friendsfootball.shared.serialized.UserResultSer;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface FriendsfootballServiceAsync {
	
	void user(String user, String password, String agent, AsyncCallback<UserResultSer> callback) throws IllegalArgumentException;

	void password(String key, String password, @SuppressWarnings("rawtypes") AsyncCallback callback) throws IllegalArgumentException;

	void sendPassword(String user, @SuppressWarnings("rawtypes") AsyncCallback callback) throws IllegalArgumentException;
	
	void places(AsyncCallback<List<PlaceSer>> callback) throws IllegalArgumentException;
	
	void addPlace(PlaceSer placeSer,AsyncCallback<PlaceSer> callback) throws IllegalArgumentException;

	void updatePlace(PlaceSer placeSer,AsyncCallback<PlaceSer> callback) throws IllegalArgumentException;

	void removePlace(PlaceSer placeSer,AsyncCallback<PlaceSer> callback) throws IllegalArgumentException;

	void matchesByPlace(PlaceSer placeSer,AsyncCallback<List<MatchSer>> callback) throws IllegalArgumentException;
		
	void matchesByRight(String userKey,AsyncCallback<List<MatchSer>> callback) throws IllegalArgumentException;
	
	void matches(String userKey,AsyncCallback<List<MatchSer>> callback) throws IllegalArgumentException;
	
	void addMatch(MatchSer matchSer,AsyncCallback<MatchSer> callback) throws IllegalArgumentException;

	void updateMatch(MatchSer matchSer,AsyncCallback<MatchSer> callback) throws IllegalArgumentException;

	void updateMatchByRight(MatchSer matchSer,AsyncCallback<MatchSer> callback) throws IllegalArgumentException;
	
	void removeMatch(MatchSer matchSer,AsyncCallback<MatchSer> callback) throws IllegalArgumentException;

	void players(AsyncCallback<List<PlayerSer>> callback) throws IllegalArgumentException;

	void addPlayer(PlayerSer playerSer,AsyncCallback<PlayerSer> callback) throws IllegalArgumentException;

	void updatePlayer(PlayerSer playerSer,AsyncCallback<PlayerSer> callback) throws IllegalArgumentException;

	void removePlayer(PlayerSer playerSer,AsyncCallback<PlayerSer> callback) throws IllegalArgumentException;

	void resetPassword(PlayerSer playerSer,AsyncCallback<PlayerSer> callback) throws IllegalArgumentException;
	
	void participants(String match,AsyncCallback<List<ParticipantSer>> callback) throws IllegalArgumentException;

	void addParticipant(ParticipantSer participantSer, AsyncCallback<ParticipantSer> callback) throws IllegalArgumentException;

	void updateParticipant(ParticipantSer participantSer, AsyncCallback<ParticipantSer> callback) throws IllegalArgumentException;
	
	void updateParticipantByMoney(ParticipantSer participantSer, AsyncCallback<ParticipantSer> callback) throws IllegalArgumentException;
	
	void updateParticipantByPotential(ParticipantSer participantSer, AsyncCallback<ParticipantSer> callback) throws IllegalArgumentException;
	
	void removeParticipant(ParticipantSer participantSer, AsyncCallback<ParticipantSer> callback) throws IllegalArgumentException;

	void sendMessage(String matchKey, String message, AsyncCallback<String> callback) throws IllegalArgumentException;

	void sendBalance(String matchKey, AsyncCallback<String> callback) throws IllegalArgumentException;
	
	void histories(AsyncCallback<List<HistorySer>> callback) throws IllegalArgumentException;

	void list1(AsyncCallback<List<List1Ser>> callback) throws IllegalArgumentException;
	
}

