package hu.safi.friendsfootball.client.datasource;

import hu.safi.friendsfootball.client.ClientConstants;
import hu.safi.friendsfootball.client.ClientLabels;
import hu.safi.friendsfootball.shared.serialized.List1Ser;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class List1DataSource
    extends GwtRpcDataSource {
	
    public List1DataSource () {
   
        DataSourceField field;
        
        field = new DataSourceTextField (ClientConstants.LIST1_USERID , ClientLabels.LIST1_USERID);
        addField (field);

        field = new DataSourceTextField (ClientConstants.LIST1_NAME, ClientLabels.LIST1_NAME);
        addField (field);
                       
        field = new DataSourceTextField (ClientConstants.LIST1_MATCH, ClientLabels.LIST1_MATCH);
        addField (field);
    
    }

    @Override
    protected void executeFetch (final String requestId, final DSRequest request, final DSResponse response) {
    	footballService.list1 (new AsyncCallback<List<List1Ser>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<List1Ser> result) {            	
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
	
    private static void copyValues (List1Ser from, ListGridRecord to) {
	    	 to.setAttribute (ClientConstants.LIST1_USERID, from.getUserId());
	       	 to.setAttribute (ClientConstants.LIST1_NAME, from.getName());
	       	 to.setAttribute (ClientConstants.LIST1_MATCH, from.getMatch());
    }	
    
}
