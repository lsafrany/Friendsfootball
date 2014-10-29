package hu.safi.friendsfootball.shared.serialized;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ResultSer implements IsSerializable {

	private String error = null;
				
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

}
