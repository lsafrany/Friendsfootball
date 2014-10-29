package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.client.datasource.MatchDataSource;
import hu.safi.friendsfootball.client.datasource.ParticipantDataSource;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
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
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.DataArrivedEvent;
import com.smartgwt.client.widgets.grid.events.DataArrivedHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class Query {	

	private final FriendsfootballServiceAsync footballService = GWT
	.create(FriendsfootballService.class);

	HLayout hLayout = new HLayout();
	
	public HLayout getLayout() {
		return this.hLayout;
	}

	public Query(final String userKey,final String groupId) {
		
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);		
		hLayout.setWidth("100%"); 
		hLayout.setHeight("100%");
		hLayout.setMargin(10);   
		hLayout.setAlign(VerticalAlignment.TOP);
	
		VLayout matchLayout = new VLayout(); 
		matchLayout.setWidth("50%");
		matchLayout.setHeight("70%");
		matchLayout.setMargin(10);  
		matchLayout.setAlign(VerticalAlignment.TOP);
		matchLayout.setDefaultLayoutAlign(Alignment.CENTER);		
		
		VLayout participantLayout = new VLayout();
		participantLayout.setWidth("40%");
		participantLayout.setHeight("100%");
		participantLayout.setMargin(10);  
		participantLayout.setAlign(VerticalAlignment.TOP);
		participantLayout.setDefaultLayoutAlign(Alignment.CENTER);		
										
		final ListGrid matchGrid = new ListGrid();
		matchGrid.setWidth("70%");
		matchGrid.setTitle(ClientLabels.MATCHES);
		matchGrid.setShowAllRecords(true);  
		matchGrid.setSortField(ClientConstants.MATCH_PLACE);
 
	    final DataSource matchDataSource = new MatchDataSource() {	    		    	
            @Override  
            protected Object transformRequest(DSRequest dsRequest) { 
            	DisplayRequest.processStart();  
            	dsRequest.setAttribute(ClientConstants.ATTRIBUTE, ClientConstants.ATTRIBUTE_ALL);
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
	    matchGrid.setFields(matchListGridField1,matchListGridField2,matchListGridField3);
			    
    	VLayout matchBottomLayout = new VLayout();
    	matchBottomLayout.setAlign(Alignment.CENTER);
    	matchBottomLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);		
    	matchBottomLayout.setWidth("70%");
    	matchBottomLayout.setHeight("25%");

    	final DynamicForm form = new DynamicForm();     
		final TextAreaItem textAreaItem = new TextAreaItem();
		textAreaItem.setWidth("100%");
	    textAreaItem.setTitle("Üzenet");
	    form.setFields(textAreaItem);
	    
		HLayout sendLayout = new HLayout();
		sendLayout.setDefaultLayoutAlign(Alignment.CENTER);
		sendLayout.setAlign(VerticalAlignment.CENTER);
		sendLayout.setWidth("20%");
	    final IButton sendButton = new IButton(ClientLabels.SEND);	
	    sendButton.setDisabled(true);
		sendLayout.addMember(sendButton);
						
	    matchBottomLayout.addMember(form);
		matchBottomLayout.addMember(sendLayout);		
		
		final ListGrid participantGrid = new ListGrid();   
		participantGrid.setWidth("80%");
		participantGrid.setHeight("100%");
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
               	dsRequest.setAttribute(ClientConstants.ATTRIBUTE, ClientConstants.ATTRIBUTE_POTENTIAL);
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
	    ListGridField participantListGridField2 = new ListGridField(ClientConstants.PARTICIPANT_POTENTIAL);	   
	    participantGrid.setFields(participantListGridField1,participantListGridField2);
    	
    	HLayout participantBottomLayout = new HLayout();
     	participantBottomLayout.setDefaultLayoutAlign(Alignment.CENTER);	
    	participantBottomLayout.setHeight("5%");		
    	participantBottomLayout.setWidth("60%");
		
		HLayout refreshLayout = new HLayout();
		refreshLayout.setDefaultLayoutAlign(Alignment.CENTER);
		refreshLayout.setAlign(VerticalAlignment.CENTER);
		refreshLayout.setWidth("50%");
	    final IButton refreshButton = new IButton(ClientLabels.REFRESH + " > 300");	    
		refreshLayout.addMember(refreshButton);
		
		HLayout countLayout = new HLayout();
		countLayout.setDefaultLayoutAlign(Alignment.CENTER);
		countLayout.setAlign(VerticalAlignment.CENTER);
		countLayout.setWidth("50%");
		final Label countLabel = new Label("1");
		countLabel.setAlign(Alignment.CENTER);
		countLayout.addMember(countLabel);		
	
		participantBottomLayout.addMember(refreshLayout);
		participantBottomLayout.addMember(countLayout);		
		
		matchGrid.addRecordClickHandler(new RecordClickHandler() {   
            public void onRecordClick(RecordClickEvent event) {
            	Criteria criteria = new Criteria();
            	String matchKey = event.getRecord().getAttributeAsString(ClientConstants.MATCH_KEY);
            	criteria.addCriteria(ClientConstants.PARTICIPANT_MATCH,matchKey);
            	participantGrid.fetchData(criteria);
				Date now = new Date();
			    Date expire = new Date(now.getTime() - (ClientConstants.COOKIE_EXPIRE * 86400000));
				Cookies.setCookie(ClientConstants.COOKIE_MATCH, matchKey, expire);
				countLabel.setContents("1");
			    sendButton.setDisabled(false);
            }
        });   
				
	    matchGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {
			    String matchKey = "";
				if (Cookies.getCookie(ClientConstants.COOKIE_MATCH) != null) matchKey = Cookies.getCookie(ClientConstants.COOKIE_MATCH);
				if (!matchKey.equals("")) {
					for (int i=0;i<matchGrid.getRecords().length;i++) {
						if (matchGrid.getRecord(i).getAttributeAsString(ClientConstants.MATCH_KEY).equals(matchKey)) {	
						    sendButton.setDisabled(false);
							matchGrid.selectRecord(matchGrid.getRecord(i));
							i = matchGrid.getRecords().length;
				           	Criteria criteria = new Criteria();
			            	criteria.addCriteria(ClientConstants.PARTICIPANT_MATCH,matchKey);
			            	participantGrid.fetchData(criteria);							   
						}
					}            						
					
				}	
			}
		});	    
	    
	    participantGrid.addDataArrivedHandler(new DataArrivedHandler() {
			public void onDataArrived(DataArrivedEvent event) {
				String tmpKey;
				for (int i=0;i<participantGrid.getRecords().length;i++) {
					if (participantGrid.getRecord(i).getAttributeAsString(ClientConstants.PARTICIPANT_ID).contains(ClientConstants.GROUP_DELIMITER)) {
						tmpKey = participantGrid.getRecord(i).getAttributeAsString(ClientConstants.PARTICIPANT_ID).substring(0,participantGrid.getRecord(i).getAttributeAsString(ClientConstants.PARTICIPANT_ID).lastIndexOf(ClientConstants.GROUP_DELIMITER));
						if (!tmpKey.equals(groupId)) {
							participantGrid.getRecord(i).setEnabled(false);
						}
					}
					else {
						if (!participantGrid.getRecord(i).getAttributeAsString(ClientConstants.PARTICIPANT_ID).equals(groupId)) {						
							participantGrid.getRecord(i).setEnabled(false);
						}
					}	
				}	
			}
		});	    

	    refreshButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {
	    		if (matchGrid.getSelectedRecord() != null) {
	    			Criteria criteria = new Criteria();	           	
	    			criteria.addCriteria(ClientConstants.PARTICIPANT_MATCH,matchGrid.getSelectedRecord().getAttributeAsString(ClientConstants.MATCH_KEY));
	    			participantGrid.invalidateCache();
	    			participantGrid.fetchData(criteria);
	    			countLabel.setContents("1");
	    		}		
  	    	}	
        });         	    	    	 
	    
	    sendButton.addClickHandler(new ClickHandler() {  
	    	public void onClick(ClickEvent event) {	
	    		if ((textAreaItem !=null) &&  (textAreaItem.getValue() != null) && (textAreaItem.getValue().toString().length() > 0)) {
	    			sendButton.setDisabled(true);
		    		DisplayRequest.processStart();	    		
	    			footballService.sendMessage( matchGrid.getSelectedRecord().getAttributeAsString(ClientConstants.MATCH_KEY),textAreaItem.getValue().toString(),new AsyncCallback<String>() {
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
	    	 }	
        });  
		matchLayout.addMember(matchGrid);
		matchLayout.addMember(matchBottomLayout);
		
		participantLayout.addMember(participantGrid);		
		participantLayout.addMember(participantBottomLayout);

	    hLayout.addMember(matchLayout);	
	    hLayout.addMember(participantLayout);	
	    	    	
		new Timer() {  
            public void run() {   
            	Integer count = Integer.valueOf(countLabel.getContents());
            	count++;
            	if (count > 300) {     
            		if (matchGrid.getSelectedRecord() != null) {
            			Criteria criteria = new Criteria();	           	
            			criteria.addCriteria(ClientConstants.PARTICIPANT_MATCH,matchGrid.getSelectedRecord().getAttributeAsString(ClientConstants.MATCH_KEY));
	            		participantGrid.invalidateCache();
	            		participantGrid.fetchData(criteria);
            		}  
            		count = 1;
            	}            		
            	countLabel.setContents(count.toString());
           		schedule(ClientConstants.REFRESH_SCHEDULE);           		
            }  
        }.schedule(ClientConstants.REFRESH_SCHEDULE);  
	    
	}

}
