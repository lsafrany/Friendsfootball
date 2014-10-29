package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.client.datasource.MatchDataSource;
import hu.safi.friendsfootball.client.datasource.ParticipantDataSource;
import hu.safi.friendsfootball.client.datasource.PlayerDataSource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.RowEndEditAction;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Team {	

	private final FriendsfootballServiceAsync footballService = GWT
	.create(FriendsfootballService.class);

	HLayout hLayout = new HLayout();

	String key = "";
	String name = "";
		
	public HLayout getLayout() {
		return this.hLayout;
	}

	public Team(final String userKey) {
		
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);		
		hLayout.setWidth("100%"); 
		hLayout.setHeight("100%");
		hLayout.setMargin(10);   
		hLayout.setAlign(VerticalAlignment.TOP);
	
		VLayout matchLayout = new VLayout(); 
		matchLayout.setWidth("30%");
		matchLayout.setHeight("40%");
		matchLayout.setMargin(10);  
		matchLayout.setAlign(VerticalAlignment.TOP);
		matchLayout.setDefaultLayoutAlign(Alignment.CENTER);		
		
		VLayout participantLayout = new VLayout();
		participantLayout.setWidth("30%");
		participantLayout.setHeight("100%");
		participantLayout.setMargin(10);  
		participantLayout.setAlign(VerticalAlignment.TOP);
		participantLayout.setDefaultLayoutAlign(Alignment.CENTER);		
							
		VLayout playerLayout = new VLayout();
		playerLayout.setWidth("40%");
		playerLayout.setHeight("90%");
		playerLayout.setMargin(10);  
		playerLayout.setAlign(VerticalAlignment.TOP);
		playerLayout.setDefaultLayoutAlign(Alignment.CENTER);	
		
		final ListGrid matchGrid = new ListGrid();
		matchGrid.setWidth("90%");
		matchGrid.setTitle(ClientLabels.MATCHES);
		matchGrid.setShowAllRecords(true);  		
		matchGrid.setSortField(ClientConstants.MATCH_PLACE);
		
	    final DataSource matchDataSource = new MatchDataSource() {	    		    	
            @Override  
            protected Object transformRequest(DSRequest dsRequest) { 
            	DisplayRequest.processStart();  
            	dsRequest.setAttribute(ClientConstants.ATTRIBUTE, ClientConstants.ATTRIBUTE_RIGHT);
            	dsRequest.setAttribute(ClientConstants.PLAYER_KEY, userKey);
                return super.transformRequest(dsRequest);   
            }   
            @Override  
            protected void transformResponse(DSResponse response, DSRequest request, Object data) {            
                if (response.getStatus() == RPCResponse.STATUS_FAILURE || response.getAttribute(ClientConstants.ERROR) != null) { 
                	DisplayRequest.serverError();
                	if (response.getAttribute(ClientConstants.ERROR) != null) SC.warn(response.getAttribute(ClientConstants.ERROR)); 
        		}
                else {
                	DisplayRequest.serverResponse();
                	super.transformResponse(response, request, data);
                }  	
            }   
        };   				
		matchGrid.setDataSource(matchDataSource);
		matchGrid.setAutoFetchData(true);
 
	    ListGridField matchListGridField1 = new ListGridField(ClientConstants.MATCH_PLACE);
	    ListGridField matchListGridField2 = new ListGridField(ClientConstants.MATCH_DAY);	    
	    ListGridField matchListGridField3 = new ListGridField(ClientConstants.MATCH_STARTTIME);	  
	    ListGridField matchListGridField4 = new ListGridField(ClientConstants.MATCH_MONEY);	  
	    matchGrid.setFields(matchListGridField1,matchListGridField2,matchListGridField3,matchListGridField4);
		    
    	HLayout matchBottomLayout = new HLayout();
     	matchBottomLayout.setDefaultLayoutAlign(Alignment.CENTER);	
    	matchBottomLayout.setHeight("15%");		
    	matchBottomLayout.setWidth("60%");

		HLayout sendLayout = new HLayout();
		sendLayout.setDefaultLayoutAlign(Alignment.CENTER);
		sendLayout.setAlign(VerticalAlignment.CENTER);
		sendLayout.setWidth("50%");
	    final IButton sendButton = new IButton(ClientLabels.SEND);	    
	    sendButton.setDisabled(true);
		sendLayout.addMember(sendButton);

		matchBottomLayout.addMember(sendLayout);

		final ListGrid participantGrid = new ListGrid();   
		participantGrid.setWidth("90%");
		participantGrid.setTitle(ClientLabels.PARTICIPANTS);
		participantGrid.setShowAllRecords(true);
		participantGrid.setCanEdit(true);
		participantGrid.setEditByCell(true);
		participantGrid.setEditEvent(ListGridEditEvent.CLICK);
		participantGrid.setListEndEditAction(RowEndEditAction.NONE);  
		participantGrid.setSortField(ClientConstants.PARTICIPANT_NAME);

	    final DataSource participantDataSource = new ParticipantDataSource() {	    		    	
            @Override  
            protected Object transformRequest(DSRequest dsRequest) { 
            	DisplayRequest.processStart();  
            	dsRequest.setAttribute(ClientConstants.ATTRIBUTE, ClientConstants.ATTRIBUTE_MONEY);
                return super.transformRequest(dsRequest);  
            }   
            @Override  
            protected void transformResponse(DSResponse response, DSRequest request, Object data) {            
                if (response.getStatus() == RPCResponse.STATUS_FAILURE || response.getAttribute(ClientConstants.ERROR) != null) { 
                	DisplayRequest.serverError();
                	if (response.getAttribute(ClientConstants.ERROR) != null) SC.warn(response.getAttribute(ClientConstants.ERROR)); 
        		}
                else {
                	DisplayRequest.serverResponse();
                	super.transformResponse(response, request, data);
                }  	
            }   
        };   				
    	participantGrid.setDataSource(participantDataSource);   

	    ListGridField participantListGridField1 = new ListGridField(ClientConstants.PARTICIPANT_NAME);
	    ListGridField participantListGridField2 = new ListGridField(ClientConstants.PARTICIPANT_MONEY);	   
	    participantGrid.setFields(participantListGridField1,participantListGridField2);
  	    	    
    	HLayout participantBottomLayout = new HLayout();
     	participantBottomLayout.setHeight("10%");		
    	participantBottomLayout.setWidth("60%");

		HLayout participantAddButtonLayout = new HLayout();
		participantAddButtonLayout.setWidth("20%");
	    final IButton participantAddButton = new IButton(ClientLabels.ADD);  
	    participantAddButton.disable();
	    participantAddButtonLayout.addMember(participantAddButton);

		HLayout countLayout = new HLayout();
		countLayout.setAlign(Alignment.CENTER);
		final Label countLabel = new Label("");
		countLabel.setAlign(Alignment.CENTER);
		countLayout.addMember(countLabel);		
	    	    
		HLayout participantRemoveButtonLayout = new HLayout();
		participantRemoveButtonLayout.setAlign(Alignment.RIGHT);
		participantRemoveButtonLayout.setWidth("20%");
	    final IButton participantRemoveButton = new IButton(ClientLabels.REMOVE);  
	    participantRemoveButton.disable();
	    participantRemoveButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		participantGrid.removeSelectedData();
	    	}	
        });    
	    participantRemoveButtonLayout.addMember(participantRemoveButton);
	    
	    participantBottomLayout.addMember(participantAddButtonLayout);
		participantBottomLayout.addMember(countLayout);		
	    participantBottomLayout.addMember(participantRemoveButtonLayout);						
   	    
	    
		HLayout selectedPlayerLayout = new HLayout();
		selectedPlayerLayout.setAlign(Alignment.CENTER);	
		selectedPlayerLayout.setWidth("100%");
		selectedPlayerLayout.setHeight("10%");
		final Label selectedPlayerLabel = new Label();   		
		selectedPlayerLayout.addMember(selectedPlayerLabel);

		final ListGrid playerGrid = new ListGrid();   
		playerGrid.setShowFilterEditor(true);    
		playerGrid.setWidth("90%");
		playerGrid.setTitle(ClientLabels.PLAYERS);
		playerGrid.setShowAllRecords(true);
		playerGrid.setCanEdit(false);
		playerGrid.setSortField(ClientConstants.PLAYER_ID);

	    final DataSource playerDataSource = new PlayerDataSource() {	    		    	
            @Override  
            protected Object transformRequest(DSRequest dsRequest) { 
            	DisplayRequest.processStart();  
                return super.transformRequest(dsRequest);   
            }   
            @Override  
            protected void transformResponse(DSResponse response, DSRequest request, Object data) {            
                if (response.getStatus() == RPCResponse.STATUS_FAILURE || response.getAttribute(ClientConstants.ERROR) != null) { 
                	DisplayRequest.serverError();
                	if (response.getAttribute(ClientConstants.ERROR) != null) SC.warn(response.getAttribute(ClientConstants.ERROR)); 
        		}
                else {
                	DisplayRequest.serverResponse();
                	super.transformResponse(response, request, data);
                }  	
            }   
        };   				
		playerGrid.setDataSource(playerDataSource);   
		
		matchGrid.addRecordClickHandler(new RecordClickHandler() {   
            public void onRecordClick(RecordClickEvent event) {
            	Criteria criteria = new Criteria();
            	String matchKey = event.getRecord().getAttributeAsString(ClientConstants.MATCH_KEY);
            	criteria.addCriteria(ClientConstants.PARTICIPANT_MATCH,matchKey);
            	participantGrid.fetchData(criteria);
           	    sendButton.setDisabled(false);
            }
        });   
			
		participantGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {
				Integer count = new Integer(0);
				for (int i=0;i<participantGrid.getRecords().length;i++) {
					count = count + participantGrid.getRecord(i).getAttributeAsInt(ClientConstants.PARTICIPANT_MONEY);
				}
				countLabel.setContents(count.toString());
			}
		});	    
		
		participantGrid.addEditCompleteHandler(new EditCompleteHandler() {  
			public void onEditComplete(EditCompleteEvent event) {
				Integer count = new Integer(0);
				for (int i=0;i<participantGrid.getRecords().length;i++) {
					count = count + participantGrid.getRecord(i).getAttributeAsInt(ClientConstants.PARTICIPANT_MONEY);
				}
				countLabel.setContents(count.toString());
 			}   
        });           		
		
	    sendButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		sendButton.setDisabled(true);
	    		DisplayRequest.processStart();	    		
    			footballService.sendBalance( matchGrid.getSelectedRecord().getAttributeAsString(ClientConstants.MATCH_KEY),new AsyncCallback<String>() {
	    			public void onFailure(Throwable caught) {
	    				DisplayRequest.serverError(); 	
	    				sendButton.setDisabled(false);
	    			}
	    			public void onSuccess(String result) {		        			
	    				DisplayRequest.serverResponse();
	    				if (result != null) SC.warn(result);
	    				else SC.say(ClientConstants.SENT);
	    				sendButton.setDisabled(false);
	    			}
	    		});
	    	 }	
        });  

	    participantGrid.addRecordClickHandler(new RecordClickHandler() {    
            public void onRecordClick(RecordClickEvent event) {
            	participantRemoveButton.enable();     
            }
        });   	    

	    playerGrid.addRecordClickHandler(new RecordClickHandler() {    
            public void onRecordClick(RecordClickEvent event) {
            	
            	boolean found = false;            	
            	for (int i=0;i<participantGrid.getRecords().length;i++) {	
					if (participantGrid.getRecord(i).getAttributeAsString(ClientConstants.PARTICIPANT_PLAYER).equals(event.getRecord().getAttributeAsString(ClientConstants.PLAYER_KEY))) found = true;						     
   	  			} 
            	
            	if (found) participantAddButton.disable();
            	else participantAddButton.enable();
            	key = (event.getRecord().getAttributeAsString(ClientConstants.PLAYER_KEY));
            	name = (event.getRecord().getAttributeAsString(ClientConstants.PLAYER_NAME));            	           	
             	selectedPlayerLabel.setContents(event.getRecord().getAttributeAsString(ClientConstants.PLAYER_NAME));
            }
        });   	    

	    participantAddButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {
	    		ListGridRecord listGridRecord = new ListGridRecord();
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_NAME, name);
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_MATCH, matchGrid.getSelectedRecord().getAttributeAsString(ClientConstants.MATCH_KEY));	    	
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_PLAYER, key);
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_RIGHT, "N");	    		
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_MONEY, 0);
	    		participantGrid.addData(listGridRecord);
   	    	}	
        });         	    	    	

		matchLayout.addMember(matchGrid);
		matchLayout.addMember(matchBottomLayout);
		
		participantLayout.addMember(participantGrid);		
		participantLayout.addMember(participantBottomLayout);

		playerLayout.addMember(selectedPlayerLayout);
		playerLayout.addMember(playerGrid);

	    hLayout.addMember(matchLayout);	
	    hLayout.addMember(participantLayout);
	    hLayout.addMember(playerLayout);	
	    	    		    
	}

}
