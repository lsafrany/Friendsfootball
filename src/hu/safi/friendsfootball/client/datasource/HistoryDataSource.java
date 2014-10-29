package hu.safi.friendsfootball.client.datasource;

import hu.safi.friendsfootball.client.ClientConstants;
import hu.safi.friendsfootball.client.ClientLabels;
import hu.safi.friendsfootball.shared.serialized.HistorySer;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class HistoryDataSource
    extends GwtRpcDataSource {
	
    public HistoryDataSource () {
   
        DataSourceField field;
        
        field = new DataSourceTextField (ClientConstants.HISTORY_KEY, ClientLabels.HISTORY_KEY);
        field.setPrimaryKey (true);
        field.setHidden(true);
        addField (field);

        field = new DataSourceTextField (ClientConstants.HISTORY_USERID, ClientLabels.HISTORY_USERID);
        addField (field);
                       
        field = new DataSourceTextField (ClientConstants.HISTORY_MESSAGE, ClientLabels.HISTORY_MESSAGE);
        addField (field);
  
        field = new DataSourceDateTimeField (ClientConstants.HISTORY_DATE, ClientLabels.HISTORY_DATE);
        addField (field);
  
    }

    @Override
    protected void executeFetch (final String requestId, final DSRequest request, final DSResponse response) {
    	footballService.histories (new AsyncCallback<List<HistorySer>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<HistorySer> result) {            	
                ListGridRecord[] list = new ListGridRecord[result.size ()];
                if ((result.size() > 0) && (result.get(0).getError() != null)) response.setAttribute(ClientConstants.ERROR, result.get(0).getError());                
                for (int i = 0; i < list.length; i++) {
                    ListGridRecord record = new ListGridRecord ();
                    copyValues (result.get (i), record);
                    list[i] = record;
                }
                response.setData (list);
                processResponse (requestId, response);
            }
        });
    }

    @Override
    protected void executeAdd (final String requestId, final DSRequest request, final DSResponse response) {
 
    }

    @Override
    protected void executeUpdate (final String requestId, final DSRequest request, final DSResponse response) {
    }
    
	@Override
    protected void executeRemove (final String requestId, final DSRequest request, final DSResponse response) {

    }
	

    private static void copyValues (HistorySer from, ListGridRecord to) {
	    	 to.setAttribute (ClientConstants.HISTORY_KEY, from.getKey());
	       	 to.setAttribute (ClientConstants.HISTORY_USERID, from.getUserId());
	       	 to.setAttribute (ClientConstants.HISTORY_MESSAGE, from.getMessage());
	      	 to.setAttribute (ClientConstants.HISTORY_DATE, from.getDate());
    }	
    
}
