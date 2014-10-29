package hu.safi.friendsfootball.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class List1Ser implements IsSerializable {
	
	private String error = null; 
	
    private String userId;    
    
    private String name;  
    
    private String match;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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
    
}
