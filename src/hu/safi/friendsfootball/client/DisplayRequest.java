package hu.safi.friendsfootball.client;

import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.Label;

public class DisplayRequest {

	private static Progressbar progressBar = new Progressbar();
	private static int progressBarValue = ClientConstants.PROGRESS_STOP;
	private static Label statusLabel = new Label();
		
	public static Progressbar getProgressBar() {
		return progressBar;
	}

	public static void setProgressBar(Progressbar progressBar) {
		DisplayRequest.progressBar = progressBar;
	}

	public static int getProgressBarValue() {
		return progressBarValue;
	}

	public static void setProgressBarValue(int progressBarValue) {
		DisplayRequest.progressBarValue = progressBarValue;
	}

	public static Label getStatusLabel() {
		return statusLabel;
	}

	public static void setStatusLabel(Label statusLabel) {
		DisplayRequest.statusLabel = statusLabel;
	}

	public DisplayRequest() {

	}
 	
	public static void processStart () {
		progressBarValue = ClientConstants.PROGRESS_START;
	}	

	public static void serverError () {
		progressBarValue = ClientConstants.PROGRESS_STOP;
		progressBar.setPercentDone(0);	
		statusLabel.setContents("&nbsp;" + ClientLabels.SERVER_ERROR);
	}	
		
	public static void serverResponse () {
		progressBarValue = ClientConstants.PROGRESS_STOP;
		progressBar.setPercentDone(0); 	
		statusLabel.setContents("&nbsp;");
	}	

}
