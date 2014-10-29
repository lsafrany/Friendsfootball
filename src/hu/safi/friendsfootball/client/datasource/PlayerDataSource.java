package hu.safi.friendsfootball.client.datasource;

import hu.safi.friendsfootball.client.ClientConstants;
import hu.safi.friendsfootball.client.ClientLabels;
import hu.safi.friendsfootball.shared.serialized.PlayerSer;

import java.util.LinkedHashMap;
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

public class PlayerDataSource
    extends GwtRpcDataSource {
	
    public PlayerDataSource () {
   
        DataSourceField field;
        
        field = new DataSourceTextField (ClientConstants.PLAYER_KEY, ClientLabels.PLAYER_KEY);
        field.setPrimaryKey (true);
        field.setHidden(true);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PLAYER_ID, ClientLabels.PLAYER_ID);
        addField (field);
                       
        field = new DataSourceTextField (ClientConstants.PLAYER_NAME, ClientLabels.PLAYER_NAME);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PLAYER_EMAIL, ClientLabels.PLAYER_EMAIL);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PLAYER_RIGHT, ClientLabels.PLAYER_RIGHT);
        LinkedHashMap<String, String> rightHashMap = new LinkedHashMap<String, String>();
        rightHashMap.put("Y", ClientLabels.YES);   
        rightHashMap.put("N", ClientLabels.NO);   
        
        field.setValueMap(rightHashMap);
 
        addField (field);

    }

    @Override
    protected void executeFetch (final String requestId, final DSRequest request, final DSResponse response) {
    	footballService.players (new AsyncCallback<List<PlayerSer>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<PlayerSer> result) {            	
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
        PlayerSer placeyerSer = new PlayerSer ();
        copyValues (rec, placeyerSer);
        footballService.addPlayer (placeyerSer, new AsyncCallback<PlayerSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (PlayerSer result) {
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
        PlayerSer playerSer = new PlayerSer ();
        copyValues (rec, playerSer);
        footballService.updatePlayer (playerSer, new AsyncCallback<PlayerSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (PlayerSer result) {
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
        PlayerSer playerSer = new PlayerSer ();
        copyValues (rec, playerSer);
        footballService.removePlayer (playerSer, new AsyncCallback<PlayerSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (PlayerSer result) {
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

	
	private static void copyValues (ListGridRecord from, PlayerSer to) {
		to.setKey(from.getAttributeAsString (ClientConstants.PLAYER_KEY));
		to.setId(from.getAttributeAsString (ClientConstants.PLAYER_ID));		
		to.setName(from.getAttributeAsString (ClientConstants.PLAYER_NAME));
		to.setEmail(from.getAttributeAsString (ClientConstants.PLAYER_EMAIL));
		to.setRight(from.getAttributeAsString (ClientConstants.PLAYER_RIGHT));				
    }

	
    private static void copyValues (PlayerSer from, ListGridRecord to) {
    	 to.setAttribute (ClientConstants.PLAYER_KEY, from.getKey());
       	 to.setAttribute (ClientConstants.PLAYER_ID, from.getId());
       	 to.setAttribute (ClientConstants.PLAYER_NAME, from.getName());
      	 to.setAttribute (ClientConstants.PLAYER_EMAIL, from.getEmail());
    	 to.setAttribute (ClientConstants.PLAYER_RIGHT, from.getRight());
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
