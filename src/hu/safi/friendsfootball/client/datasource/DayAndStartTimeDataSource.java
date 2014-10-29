package hu.safi.friendsfootball.client.datasource;

import hu.safi.friendsfootball.client.ClientConstants;
import hu.safi.friendsfootball.client.ClientLabels;
import hu.safi.friendsfootball.shared.serialized.MatchSer;
import hu.safi.friendsfootball.shared.serialized.PlaceSer;

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

public class DayAndStartTimeDataSource
    extends GwtRpcDataSource {

   	PlaceSer placeSer = new PlaceSer();

    public DayAndStartTimeDataSource () {
   
        DataSourceField field;
        
        field = new DataSourceTextField (ClientConstants.MATCH_KEY, ClientLabels.MATCH_KEY);
        field.setPrimaryKey (true);
        field.setHidden(true);
        addField (field);
                       
        field = new DataSourceTextField (ClientConstants.MATCH_DAY, ClientLabels.MATCH_DAY);
        LinkedHashMap<String, String> dayHashMap = new LinkedHashMap<String, String>();
        dayHashMap.put("1", ClientLabels.DAY1);   
        dayHashMap.put("2", ClientLabels.DAY2);   
        dayHashMap.put("3", ClientLabels.DAY3);
        dayHashMap.put("4", ClientLabels.DAY4);   
        dayHashMap.put("5", ClientLabels.DAY5);   
        dayHashMap.put("6", ClientLabels.DAY6);
        dayHashMap.put("7", ClientLabels.DAY7);
        field.setValueMap(dayHashMap);
        addField (field);
 
        field = new DataSourceTextField (ClientConstants.MATCH_STARTTIME, ClientLabels.MATCH_STARTTIME);
        LinkedHashMap<String, String> timeHashMap = new LinkedHashMap<String, String>();
        timeHashMap.put("08:00", "08:00");   
        timeHashMap.put("08:30", "08:30");   
        timeHashMap.put("09:00", "09:00");   
        timeHashMap.put("09:30", "09:30");   
        timeHashMap.put("10:00", "10:00");   
        timeHashMap.put("10:30", "10:30");   
        timeHashMap.put("11:00", "11:00");   
        timeHashMap.put("11:30", "11:30");   
        timeHashMap.put("12:00", "12:00");   
        timeHashMap.put("12:30", "12:30");        
        timeHashMap.put("13:00", "13:00");   
        timeHashMap.put("13:30", "13:30");   
        timeHashMap.put("14:00", "14:00");   
        timeHashMap.put("14:30", "14:30");   
        timeHashMap.put("15:00", "15:00");   
        timeHashMap.put("15:30", "15:30");   
        timeHashMap.put("16:00", "16:00");   
        timeHashMap.put("16:30", "16:30");   
        timeHashMap.put("17:00", "17:00");   
        timeHashMap.put("17:30", "17:30");
        timeHashMap.put("18:00", "18:00");
        timeHashMap.put("18:30", "18:30");   
        timeHashMap.put("19:00", "19:00");   
        timeHashMap.put("19:30", "19:30");
        timeHashMap.put("20:00", "20:00");
        timeHashMap.put("20:30", "20:30");
        timeHashMap.put("21:00", "21:00");
        timeHashMap.put("21:30", "21:30");
        timeHashMap.put("22:00", "22:00");        
        
        field.setValueMap(timeHashMap);
        addField (field);
        
        field = new DataSourceIntegerField (ClientConstants.MATCH_MONEY, ClientLabels.MATCH_MONEY);
        addField (field);

    }

    @Override
    protected void executeFetch (final String requestId, final DSRequest request, final DSResponse response) {
      	placeSer.setKey(request.getCriteria().getAttribute(ClientConstants.MATCH_PLACE));
    	footballService.matchesByPlace (placeSer,new AsyncCallback<List<MatchSer>> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (List<MatchSer> result) {            	
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
        // Retrieve record which should be added.
        JavaScriptObject data = request.getData ();
        ListGridRecord rec = new ListGridRecord (data);
        MatchSer matchSer = new MatchSer ();
        copyValues (rec, matchSer);
        matchSer.setPlace(placeSer.getKey());
        footballService.addMatch (matchSer, new AsyncCallback<MatchSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (MatchSer result) {
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
        MatchSer matchSer = new MatchSer ();
        copyValues (rec, matchSer);
        footballService.updateMatch (matchSer, new AsyncCallback<MatchSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (MatchSer result) {
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
        // Retrieve record which should be removed.
        JavaScriptObject data = request.getData ();
        final ListGridRecord rec = new ListGridRecord (data);
        MatchSer matchSer = new MatchSer ();
        copyValues (rec, matchSer);
        footballService.removeMatch (matchSer, new AsyncCallback<MatchSer> () {
            public void onFailure (Throwable caught) {
                response.setStatus (RPCResponse.STATUS_FAILURE);
                processResponse (requestId, response);
            }
            public void onSuccess (MatchSer result) {
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
	
	
	private static void copyValues (ListGridRecord from, MatchSer to) {
		to.setKey(from.getAttributeAsString (ClientConstants.MATCH_KEY));
		to.setPlace(from.getAttributeAsString (ClientConstants.MATCH_PLACE));
		to.setDay(from.getAttributeAsString (ClientConstants.MATCH_DAY));
		to.setStartTime(from.getAttributeAsString (ClientConstants.MATCH_STARTTIME));
		to.setMoney(from.getAttributeAsInt (ClientConstants.MATCH_MONEY));	   		
    }

    private static void copyValues (MatchSer from, ListGridRecord to) {
    	to.setAttribute (ClientConstants.MATCH_KEY, from.getKey());
       	to.setAttribute (ClientConstants.MATCH_PLACE, from.getPlace());
       	to.setAttribute (ClientConstants.MATCH_DAY, from.getDay());
    	to.setAttribute (ClientConstants.MATCH_STARTTIME, from.getStartTime());
    	to.setAttribute (ClientConstants.MATCH_MONEY, from.getMoney());
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
