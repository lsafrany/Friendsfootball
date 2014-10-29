package hu.safi.friendsfootball.client;

import hu.safi.friendsfootball.client.datasource.List1DataSource;

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

public class List1 {	

	HLayout hLayout = new HLayout();
	
	public HLayout getLayout() {
		return this.hLayout;
	}

	public List1() {
		
		hLayout.setDefaultLayoutAlign(VerticalAlignment.CENTER);		
		hLayout.setWidth("100%"); 
		hLayout.setHeight("100%");
		hLayout.setMargin(10);   
		hLayout.setAlign(VerticalAlignment.TOP);
	
		VLayout historyLayout = new VLayout(); 
		historyLayout.setWidth("100%");
		historyLayout.setHeight("80%");
		historyLayout.setMargin(10);  
		historyLayout.setAlign(VerticalAlignment.TOP);
		historyLayout.setDefaultLayoutAlign(Alignment.CENTER);
		
		final ListGrid historyGrid = new ListGrid();
		historyGrid.setWidth("80%");
		historyGrid.setTitle(ClientLabels.LIST1);
		historyGrid.setShowAllRecords(true);  		
 
	    final DataSource list1DataSource = new List1DataSource() {
	    	
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
        
        historyGrid.setDataSource(list1DataSource);   
        historyGrid.setAutoFetchData(true);

	    ListGridField historyListGridField1 = new ListGridField(ClientConstants.LIST1_USERID);
	    historyListGridField1.setWidth("10%");
	    ListGridField historyListGridField2 = new ListGridField(ClientConstants.LIST1_NAME);
	    historyListGridField2.setWidth("30%");
	    ListGridField historyListGridField3 = new ListGridField(ClientConstants.LIST1_MATCH);
	    historyGrid.setFields(historyListGridField1,historyListGridField2,historyListGridField3);
	    			
	    historyLayout.addMember(historyGrid);
		
		hLayout.addMember(historyLayout);			
	}
}
