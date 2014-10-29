package hu.safi.friendsfootball.shared.serialized;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HistorySer implements IsSerializable {
	
	private String error = null; 
	
    private String key;    
    
    private String userId;  
    
    private String message;  
    
    private Date date;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
    
}
