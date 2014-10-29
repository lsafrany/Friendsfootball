package hu.safi.friendsfootball.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PlayerSer implements IsSerializable {
	
	private String error = null; 
	
    private String key;    
    
    private String id;    
    
    private String name;
    
    private String email;
  
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}

}
