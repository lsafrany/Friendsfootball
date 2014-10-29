package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.client.datasource.HistoryDataSource;

import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class History {	

	HLayout hLayout = new HLayout();
	
	public HLayout getLayout() {
		return this.hLayout;
	}

	public History() {
		
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);		
		hLayout.setWidth("100%"); 
		hLayout.setHeight("100%");
		hLayout.setMargin(10);   
		hLayout.setAlign(VerticalAlignment.TOP);
	
		VLayout historyLayout = new VLayout(); 
		historyLayout.setWidth("100%");
		historyLayout.setHeight("90%");
		historyLayout.setMargin(10);  
		historyLayout.setAlign(VerticalAlignment.TOP);
		historyLayout.setDefaultLayoutAlign(Alignment.CENTER);
		
		final ListGrid historyGrid = new ListGrid();
		historyGrid.setWidth("90%");
		historyGrid.setTitle(ClientLabels.HISTORY);
		historyGrid.setShowAllRecords(true);  		
 
	    final DataSource historyDataSource = new HistoryDataSource() {	    		    	
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
        historyGrid.setDataSource(historyDataSource);   
        historyGrid.setAutoFetchData(true);
        historyGrid.setShowFilterEditor(true);    
	    ListGridField historyListGridField1 = new ListGridField(ClientConstants.HISTORY_USERID);
	    historyListGridField1.setWidth("5%");
	    ListGridField historyListGridField2 = new ListGridField(ClientConstants.HISTORY_MESSAGE);	    
	    ListGridField historyListGridField3 = new ListGridField(ClientConstants.HISTORY_DATE);
	    historyListGridField3.setWidth("10%");
	    historyGrid.setFields(historyListGridField1,historyListGridField2,historyListGridField3);
	    			
	    historyLayout.addMember(historyGrid);
		
		hLayout.addMember(historyLayout);			
	}
}
