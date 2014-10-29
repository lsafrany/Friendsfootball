package hu.safi.friendsfootball.client.datasource;

import hu.safi.friendsfootball.client.ClientConstants;
import hu.safi.friendsfootball.client.ClientLabels;
import hu.safi.friendsfootball.shared.serialized.PlaceSer;

import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PlaceDataSource
    extends GwtRpcDataSource {
	
    public PlaceDataSource () {
   
        DataSourceField field;
        
        field = new DataSourceTextField (ClientConstants.PLACE_KEY, ClientLabels.PLACE_KEY);
        field.setPrimaryKey (true);
        field.setHidden(true);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PLACE_NAME, ClientLabels.PLACE_NAME);
        field.setRequired(true);   
        addField (field);
                       
    }

    @Override
    protected void executeFetch (final String requestId, final DSRequest request, final DSResponse response) {
    	footballService.places (new AsyncCallback<List<PlaceSer>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<PlaceSer> result) {            	
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
 
        JavaScriptObject data = request.getData ();
        ListGridRecord rec = new ListGridRecord (data);
        PlaceSer placeSer = new PlaceSer ();
        copyValues (rec, placeSer);
        footballService.addPlace (placeSer, new AsyncCallback<PlaceSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (PlaceSer result) {
                if (result.getError() != null) {
             	    response.setStatus (RPCResponse.STATUS_FAILURE);
                	response.setAttribute(ClientConstants.ERROR, result.getError());           		               	
                }
                else {
                    ListGridRecord[] list = new ListGridRecord[1];
                    ListGridRecord newRec = new ListGridRecord ();
                    copyValues (result, newRec);
                    list[0] = newRec;
                    response.setData (list);                	
                }
                processResponse (requestId, response);
            }
        });
    }

    @Override
    protected void executeUpdate (final String requestId, final DSRequest request, final DSResponse response) {
  
        ListGridRecord rec = getEditedRecord (request);
        PlaceSer placeSer = new PlaceSer ();
        copyValues (rec, placeSer);
        footballService.updatePlace (placeSer, new AsyncCallback<PlaceSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (PlaceSer result) {
                if (result.getError() != null) {
             	    response.setStatus (RPCResponse.STATUS_FAILURE);
                	response.setAttribute(ClientConstants.ERROR, result.getError());           		               	
                }
                else {
                	ListGridRecord[] list = new ListGridRecord[1];
                	ListGridRecord updRec = new ListGridRecord ();
                	copyValues (result, updRec);
                	list[0] = updRec;
                	response.setData (list);
                }
                processResponse (requestId, response);
            }
        });
    }
    
	@Override
    protected void executeRemove (final String requestId, final DSRequest request, final DSResponse response) {

		JavaScriptObject data = request.getData ();
        final ListGridRecord rec = new ListGridRecord (data);
        PlaceSer placeSer = new PlaceSer ();
        copyValues (rec, placeSer);
        footballService.removePlace (placeSer, new AsyncCallback<PlaceSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (PlaceSer result) {
                if (result.getError() != null) {	
               	 	response.setStatus (RPCResponse.STATUS_FAILURE);
                	response.setAttribute(ClientConstants.ERROR, result);
            	}	
               	else {	
               		ListGridRecord[] list = new ListGridRecord[1];
               		// We do not receive removed record from server.
               		// Return record from request.
               		list[0] = rec;
               		response.setData (list);
               	}	
               	processResponse (requestId, response);	
            }

        });
    }
	
	
	private static void copyValues (ListGridRecord from, PlaceSer to) {
		to.setKey(from.getAttributeAsString (ClientConstants.PLACE_KEY));
		to.setName(from.getAttributeAsString (ClientConstants.PLACE_NAME));	   		
    }

    private static void copyValues (PlaceSer from, ListGridRecord to) {
    	to.setAttribute (ClientConstants.PLACE_KEY, from.getKey());
       	to.setAttribute (ClientConstants.PLACE_NAME, from.getName());
     }

    private ListGridRecord getEditedRecord (DSRequest request) {
        // Retrieving values before edit
        JavaScriptObject oldValues = request.getAttributeAsJavaScriptObject ("oldValues");
        // Creating new record for combining old values with changes
        ListGridRecord newRecord = new ListGridRecord ();
        // Copying properties from old record
        JSOHelper.apply (oldValues, newRecord.getJsObj ());
        // Retrieving changed values
        JavaScriptObject data = request.getData ();
        // Apply changes
        JSOHelper.apply (data, newRecord.getJsObj ());
        return newRecord;
    }
}
