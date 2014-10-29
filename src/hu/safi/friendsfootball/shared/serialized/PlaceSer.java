package hu.safi.friendsfootball.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PlaceSer implements IsSerializable {
	
	private String error = null; 
	
    private String key;    
    
    private String name;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
  
}
