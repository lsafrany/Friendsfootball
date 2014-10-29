package hu.safi.friendsfootball.client.datasource;

import hu.safi.friendsfootball.client.ClientConstants;
import hu.safi.friendsfootball.client.ClientLabels;
import hu.safi.friendsfootball.shared.serialized.ParticipantSer;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ParticipantDataSource
    extends GwtRpcDataSource {
	
    public ParticipantDataSource () {
   
        DataSourceField field;
                
        field = new DataSourceTextField (ClientConstants.PARTICIPANT_KEY, ClientLabels.PARTICIPANT_KEY);
        field.setHidden(true);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PARTICIPANT_ID, ClientLabels.PARTICIPANT_ID);
        field.setHidden(true);
        addField (field);
        
        field = new DataSourceTextField (ClientConstants.PARTICIPANT_NAME, ClientLabels.PARTICIPANT_NAME);
        field.setCanEdit(false);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PARTICIPANT_MATCH, ClientConstants.PARTICIPANT_MATCH);
        field.setPrimaryKey (true);
        field.setHidden(true);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PARTICIPANT_PLAYER, ClientConstants.PARTICIPANT_PLAYER);
        field.setPrimaryKey (true);
        field.setHidden(true);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PARTICIPANT_POTENTIAL, ClientLabels.PARTICIPANT_POTENTIAL);
        LinkedHashMap<String, String> potentialHashMap = new LinkedHashMap<String, String>();
        potentialHashMap.put("", ClientLabels.NOTDEFINED);   
        potentialHashMap.put("N", ClientLabels.NO);
        potentialHashMap.put("Y", ClientLabels.YES);
        field.setCanEdit(true);
        field.setValueMap(potentialHashMap);
        addField (field);
        
        field = new DataSourceIntegerField (ClientConstants.PARTICIPANT_MONEY, ClientLabels.PARTICIPANT_MONEY);
        field.setCanEdit(true);
        addField (field);

        field = new DataSourceTextField (ClientConstants.PARTICIPANT_RIGHT, ClientLabels.PARTICIPANT_RIGHT);
        LinkedHashMap<String, String> rightHashMap = new LinkedHashMap<String, String>();
        rightHashMap.put("Y", ClientLabels.YES);   
        rightHashMap.put("N", ClientLabels.NO);
        field.setCanEdit(true);
        field.setValueMap(rightHashMap);
        addField (field);

    }

    @Override
    protected void executeFetch (final String requestId, final DSRequest request, final DSResponse response) {
    	footballService.participants (request.getCriteria().getAttribute(ClientConstants.PARTICIPANT_MATCH),new AsyncCallback<List<ParticipantSer>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<ParticipantSer> result) {            	
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
        ParticipantSer participantSer = new ParticipantSer ();
        copyValues (rec, participantSer);
        footballService.addParticipant (participantSer, new AsyncCallback<ParticipantSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (ParticipantSer result) {
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
        ParticipantSer participantSer = new ParticipantSer ();
        copyValues (rec, participantSer);
        
        if (request.getAttribute(ClientConstants.ATTRIBUTE).equals(ClientConstants.ATTRIBUTE_ALL)) {
            footballService.updateParticipant (participantSer, new AsyncCallback<ParticipantSer> () {
                public void onFailure (Throwable caught) {
                    response.setStatus (RPCResponse.STATUS_FAILURE);
                    processResponse (requestId, response);
                }
                public void onSuccess (ParticipantSer result) {
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
        
        else if (request.getAttribute(ClientConstants.ATTRIBUTE).equals(ClientConstants.ATTRIBUTE_MONEY)) {        	
            footballService.updateParticipantByMoney (participantSer, new AsyncCallback<ParticipantSer> () {
                public void onFailure (Throwable caught) {
                    response.setStatus (RPCResponse.STATUS_FAILURE);
                    processResponse (requestId, response);
                }
                public void onSuccess (ParticipantSer result) {
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
        else {
            footballService.updateParticipantByPotential (participantSer, new AsyncCallback<ParticipantSer> () {
                public void onFailure (Throwable caught) {
                    response.setStatus (RPCResponse.STATUS_FAILURE);
                    processResponse (requestId, response);
                }
                public void onSuccess (ParticipantSer result) {
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
    }
    
	@Override
    protected void executeRemove (final String requestId, final DSRequest request, final DSResponse response) {
		
		JavaScriptObject data = request.getData ();
        final ListGridRecord rec = new ListGridRecord (data);
        ParticipantSer participantSer = new ParticipantSer ();
        copyValues (rec, participantSer);
        footballService.removeParticipant (participantSer, new AsyncCallback<ParticipantSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (ParticipantSer result) {
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
	
	private static void copyValues (ListGridRecord from, ParticipantSer to) {
		to.setKey(from.getAttributeAsString (ClientConstants.PARTICIPANT_KEY));
		to.setId(from.getAttributeAsString (ClientConstants.PARTICIPANT_ID));
		to.setName(from.getAttributeAsString (ClientConstants.PARTICIPANT_NAME));
		to.setMatch(from.getAttributeAsString (ClientConstants.PARTICIPANT_MATCH));
		to.setPlayer(from.getAttributeAsString (ClientConstants.PARTICIPANT_PLAYER));		
		to.setPotential(from.getAttributeAsString(ClientConstants.PARTICIPANT_POTENTIAL));		
		to.setMoney(from.getAttributeAsInt(ClientConstants.PARTICIPANT_MONEY));
		to.setRight(from.getAttributeAsString(ClientConstants.PARTICIPANT_RIGHT));		
    }

    private static void copyValues (ParticipantSer from, ListGridRecord to) {
    	 to.setAttribute (ClientConstants.PARTICIPANT_KEY, from.getKey());
    	 to.setAttribute (ClientConstants.PARTICIPANT_ID, from.getId());
       	 to.setAttribute (ClientConstants.PARTICIPANT_NAME, from.getName());
       	 to.setAttribute (ClientConstants.PARTICIPANT_MATCH, from.getMatch());
       	 to.setAttribute (ClientConstants.PARTICIPANT_PLAYER, from.getPlayer());
     	 to.setAttribute (ClientConstants.PARTICIPANT_POTENTIAL, from.getPotential());
    	 to.setAttribute (ClientConstants.PARTICIPANT_MONEY, from.getMoney());
    	 to.setAttribute (ClientConstants.PARTICIPANT_RIGHT, from.getRight());
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
