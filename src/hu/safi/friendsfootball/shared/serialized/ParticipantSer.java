package hu.safi.friendsfootball.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ParticipantSer implements IsSerializable {
	
	private String error = null; 
	
    private String key;    
    
    private String id;
    
    private String name;

    private String match;
    
    private String player;
 
    private String potential;
    
    private Integer money;    

    private String right;
    
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

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
	
	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getPotential() {
		return potential;
	}

	public void setPotential(String potential) {
		this.potential = potential;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

	
}
