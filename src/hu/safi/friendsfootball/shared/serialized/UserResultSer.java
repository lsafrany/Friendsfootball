package hu.safi.friendsfootball.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserResultSer extends ResultSer implements IsSerializable {
	
	private String key = "";
	
	private String id = "";
	
	private String name = "";
	
	private String right = "";
	
	private String participantright = "";
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	public String getParticipantright() {
		return participantright;
	}

	public void setParticipantright(String participantright) {
		this.participantright = participantright;
	}

}
