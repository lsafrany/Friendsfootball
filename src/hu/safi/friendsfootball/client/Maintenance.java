package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.client.datasource.DayAndStartTimeDataSource;
import hu.safi.friendsfootball.client.datasource.ParticipantDataSource;
import hu.safi.friendsfootball.client.datasource.PlaceDataSource;
import hu.safi.friendsfootball.client.datasource.PlayerDataSource;
import hu.safi.friendsfootball.shared.serialized.PlayerSer;

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
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;


public class Maintenance {	

	private final FriendsfootballServiceAsync footballService = GWT.create(FriendsfootballService.class);

	HLayout hLayout = new HLayout();

	String key = "";
	String name = "";

	public HLayout getLayout() {
		return this.hLayout;
	}

	public Maintenance() {
		
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);		
		hLayout.setWidth("100%"); 
		hLayout.setHeight("100%");
		hLayout.setMargin(10);   
		hLayout.setDefaultLayoutAlign(VerticalAlignment.TOP);

		VLayout placeLayout = new VLayout(); 
		placeLayout.setWidth("15%");
		placeLayout.setHeight("100%");
		placeLayout.setMargin(10);  
		placeLayout.setAlign(VerticalAlignment.TOP);
		placeLayout.setDefaultLayoutAlign(Alignment.CENTER);		

		VLayout matchLayout = new VLayout(); 
		matchLayout.setWidth("15%");
		matchLayout.setHeight("100%");
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
		playerLayout.setHeight("100%");
		playerLayout.setMargin(10);  
		playerLayout.setAlign(VerticalAlignment.TOP);
		playerLayout.setDefaultLayoutAlign(Alignment.CENTER);	
				
		final ListGrid placeGrid = new ListGrid();
		placeGrid.setWidth("100%");
		placeGrid.setHeight("90%");
		placeGrid.setTitle(ClientLabels.PLACES);
		placeGrid.setShowAllRecords(true);  
		placeGrid.setAutoFetchData(true);
		placeGrid.setCanEdit(true);
		placeGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		placeGrid.setListEndEditAction(RowEndEditAction.NONE); 
		placeGrid.setSortField(ClientConstants.PLACE_NAME); 
		
	    final DataSource placeDataSource = new PlaceDataSource() {	    		    	
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
		placeGrid.setDataSource(placeDataSource);   
		
		HLayout placeButtonLayout = new HLayout();
		placeButtonLayout.setWidth("80%");
		placeButtonLayout.setHeight("10%");
		
		HLayout placeAddButtonLayout = new HLayout();
	    IButton placeAddButton = new IButton(ClientLabels.ADD);       
	    placeAddButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {
	    		placeGrid.startEditingNew();	    		
	    	}	
        });         	    	    	
	    placeAddButtonLayout.addMember(placeAddButton);
	    	    
		HLayout placeRemoveButtonLayout = new HLayout();
		placeRemoveButtonLayout.setAlign(Alignment.RIGHT);	
	    final IButton placeRemoveButton = new IButton(ClientLabels.REMOVE);
		placeRemoveButton.disable();
	    placeRemoveButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    	   	SC.ask(ClientLabels.SURE, new BooleanCallback() {
	 		    	   public void execute(Boolean value) {
	 		    	      if (value != null && value) {		
	 		    	    		placeGrid.removeSelectedData(); 
	 		    	      }
	 		    	   }
	 	        	});	   	        		    	
	    	}	
        });    
	    placeRemoveButtonLayout.addMember(placeRemoveButton);
	    
	    placeButtonLayout.addMember(placeAddButtonLayout);
	    placeButtonLayout.addMember(placeRemoveButtonLayout);
		
		final ListGrid dayAndStartTimeGrid = new ListGrid();
		dayAndStartTimeGrid.setWidth("100%");
		dayAndStartTimeGrid.setHeight("90%");
		dayAndStartTimeGrid.setTitle(ClientLabels.MATCHES);
		dayAndStartTimeGrid.setShowAllRecords(true);  
		dayAndStartTimeGrid.setCanEdit(true);
		dayAndStartTimeGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		dayAndStartTimeGrid.setListEndEditAction(RowEndEditAction.NONE);
		dayAndStartTimeGrid.setSortField(ClientConstants.MATCH_DAY); 
		
	    final DataSource dayAndStartTimeDataSource = new DayAndStartTimeDataSource() {	    		    	
            @Override  
            protected Object transformRequest(DSRequest dsRequest) { 
            	DisplayRequest.processStart();  
            	dsRequest.setAttribute(ClientConstants.ATTRIBUTE, ClientConstants.ATTRIBUTE_ALL);
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
        dayAndStartTimeGrid.setDataSource(dayAndStartTimeDataSource);   

		HLayout dayAndStartTimeButtonLayout = new HLayout();
		dayAndStartTimeButtonLayout.setWidth("80%");
		dayAndStartTimeButtonLayout.setHeight("10%");
		
		HLayout dayAndStartTimeAddButtonLayout = new HLayout();
	    final IButton dayAndStartTimeAddButton = new IButton(ClientLabels.ADD);
	    dayAndStartTimeAddButton.disable();
	    dayAndStartTimeAddButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		dayAndStartTimeGrid.startEditingNew();	  
	    	}	
        });         	    	    	
	    dayAndStartTimeAddButtonLayout.addMember(dayAndStartTimeAddButton);
	    	    
		HLayout dayAndStartTimeRemoveButtonLayout = new HLayout();
		dayAndStartTimeRemoveButtonLayout.setAlign(Alignment.RIGHT);	
	    final IButton dayAndStartTimeRemoveButton = new IButton(ClientLabels.REMOVE);
	    dayAndStartTimeRemoveButton.disable();
	    dayAndStartTimeRemoveButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		dayAndStartTimeGrid.removeSelectedData();
	    	}	
        });    
	    dayAndStartTimeRemoveButtonLayout.addMember(dayAndStartTimeRemoveButton);
	    
	    dayAndStartTimeButtonLayout.addMember(dayAndStartTimeAddButtonLayout);
	    dayAndStartTimeButtonLayout.addMember(dayAndStartTimeRemoveButtonLayout);
		
		final ListGrid participantGrid = new ListGrid();   
		participantGrid.setWidth("100%");
		participantGrid.setHeight("90%");
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
               	dsRequest.setAttribute(ClientConstants.ATTRIBUTE, ClientConstants.ATTRIBUTE_ALL);
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
    	   	
		HLayout participantButtonLayout = new HLayout();
		participantButtonLayout.setWidth("80%");
		participantButtonLayout.setHeight("10%");
		
		HLayout participantAddButtonLayout = new HLayout();
	    final IButton participantAddButton = new IButton(ClientLabels.ADD);  
	    participantAddButton.disable();
	    participantAddButtonLayout.addMember(participantAddButton);
	    	    
		HLayout participantRemoveButtonLayout = new HLayout();
		participantRemoveButtonLayout.setAlign(Alignment.RIGHT);	
	    final IButton participantRemoveButton = new IButton(ClientLabels.REMOVE);  
	    participantRemoveButton.disable();
	    participantRemoveButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		participantGrid.removeSelectedData();
	    	}	
        });    
	    participantRemoveButtonLayout.addMember(participantRemoveButton);
	    
	    participantButtonLayout.addMember(participantAddButtonLayout);
	    participantButtonLayout.addMember(participantRemoveButtonLayout);		

		HLayout selectedPlayerLayout = new HLayout();
		selectedPlayerLayout.setAlign(Alignment.CENTER);	
		selectedPlayerLayout.setWidth("100%");
		selectedPlayerLayout.setHeight("10%");
		final Label selectedPlayerLabel = new Label();   		
		selectedPlayerLayout.addMember(selectedPlayerLabel);

		final ListGrid playerGrid = new ListGrid();   
		playerGrid.setShowFilterEditor(true);   
		playerGrid.setWidth("100%");
		playerGrid.setTitle(ClientLabels.PLAYERS);
		playerGrid.setShowAllRecords(true);
		playerGrid.setCanEdit(true);
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
		
		HLayout playerButtonLayout = new HLayout();
		playerButtonLayout.setWidth("80%");
		playerButtonLayout.setHeight("10%");
		
		HLayout playerAddButtonLayout = new HLayout();
	    IButton playerAddButton = new IButton(ClientLabels.ADD);   
	    playerAddButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		playerGrid.startEditingNew();	  
	    	}	
        });         	    	    	
	    playerAddButtonLayout.addMember(playerAddButton);
	    	    	    
		HLayout playerPasswordButtonLayout = new HLayout();
		playerPasswordButtonLayout.setAlign(Alignment.CENTER);	
	    final IButton playerPassswordButton = new IButton(ClientLabels.RESETPASSWORD);
	    playerPassswordButton.disable();
	    playerPassswordButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {
	    		PlayerSer playerSer = new PlayerSer ();	 
	    		playerSer.setKey(playerGrid.getSelectedRecord().getAttributeAsString(ClientConstants.PLAYER_KEY));
    			footballService.resetPassword(playerSer,new AsyncCallback<PlayerSer>() {
	    			public void onFailure(Throwable caught) {
	    				DisplayRequest.serverError(); 	 	
	    			}
	    			public void onSuccess(PlayerSer result) {		        			
	    				DisplayRequest.serverResponse();
	    			}
	    		});
	    	}	
        });         	    	    	
	    playerPasswordButtonLayout.addMember(playerPassswordButton);	    
	    
		HLayout playerRemoveButtonLayout = new HLayout();
		playerRemoveButtonLayout.setAlign(Alignment.RIGHT);	
	    final IButton playerRemoveButton = new IButton(ClientLabels.REMOVE);
	    playerRemoveButton.disable();
	    playerRemoveButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		playerGrid.removeSelectedData();
	    	}	
        });    
	    playerRemoveButtonLayout.addMember(playerRemoveButton);
	    
	    playerButtonLayout.addMember(playerAddButtonLayout);
	    playerButtonLayout.addMember(playerPasswordButtonLayout);
	    playerButtonLayout.addMember(playerRemoveButtonLayout);		
			    
	    placeGrid.addRecordClickHandler(new RecordClickHandler() {   
            public void onRecordClick(RecordClickEvent event) {
        		placeRemoveButton.enable();            	
           		dayAndStartTimeAddButton.enable();
        		Criteria criteria = new Criteria();
        		criteria.addCriteria(ClientConstants.MATCH_PLACE,event.getRecord().getAttributeAsString(ClientConstants.PLACE_KEY));
        		participantGrid.setData(new ListGridRecord[] {} ); 
        		dayAndStartTimeGrid.fetchData(criteria);
            }
        });   

	    dayAndStartTimeGrid.addRecordClickHandler(new RecordClickHandler() {    
            public void onRecordClick(RecordClickEvent event) {
          		dayAndStartTimeRemoveButton.enable();
         		placeRemoveButton.enable();            	
               	Criteria criteria = new Criteria();
            	criteria.addCriteria(ClientConstants.PARTICIPANT_MATCH,event.getRecord().getAttributeAsString(ClientConstants.MATCH_KEY));
            	participantGrid.fetchData(criteria);           		
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
            	           	
            	playerPassswordButton.enable();     
            	playerRemoveButton.enable();              	
            	key = (event.getRecord().getAttributeAsString(ClientConstants.PLAYER_KEY));
            	name = (event.getRecord().getAttributeAsString(ClientConstants.PLAYER_NAME));
            	selectedPlayerLabel.setContents(event.getRecord().getAttributeAsString(ClientConstants.PLAYER_NAME));
            }
        });   	    

	    participantAddButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {
	    		ListGridRecord listGridRecord = new ListGridRecord();
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_NAME, name);
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_MATCH, dayAndStartTimeGrid.getSelectedRecord().getAttributeAsString(ClientConstants.MATCH_KEY));	    	
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_PLAYER, key);
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_RIGHT, "N");	    		
	    		listGridRecord.setAttribute (ClientConstants.PARTICIPANT_MONEY, 0);
	    		participantGrid.addData(listGridRecord);
   	    	}	
        });         	    	    	
    
	    
	    
		placeLayout.addMember(placeGrid);
		placeLayout.addMember(placeButtonLayout);

		matchLayout.addMember(dayAndStartTimeGrid);
		matchLayout.addMember(dayAndStartTimeButtonLayout);
		
		participantLayout.addMember(participantGrid);
		participantLayout.addMember(participantButtonLayout);
		
		playerLayout.addMember(selectedPlayerLayout);
		playerLayout.addMember(playerGrid);
		playerLayout.addMember(playerButtonLayout);
		
		hLayout.addMember(placeLayout);	
	    hLayout.addMember(matchLayout);	
	    hLayout.addMember(participantLayout);	
	    hLayout.addMember(playerLayout);	
		
	}

}
