
// http://127.0.0.1:8888/friendsfootball/clean?day=2

package hu.safi.friendsfootball.server;

import hu.safi.friendsfootball.server.jdo.History;
import hu.safi.friendsfootball.server.jdo.Match;
import hu.safi.friendsfootball.server.jdo.PMF;
import hu.safi.friendsfootball.server.jdo.Participant;
import hu.safi.friendsfootball.server.jdo.Player;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.utils.SystemProperty;

@SuppressWarnings("serial")
public class Clean extends HttpServlet {

	private static final Logger log = Logger.getLogger(Clean.class.getName()); 
	  
	@SuppressWarnings("deprecation")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		log.info(new Date() + " start.");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<html>");
		out.append("<head>");
		out.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		out.append("<meta http-equiv=\"expires\" content=\"Mon, 22 Jul 2002 11:12:01 GMT\">");
		out.append("</head>");
		out.append("<body>");
	
		String actDay = null;
		if (request.getParameter("day") != null) {
			try {
				actDay = Integer.valueOf(request.getParameter("day")).toString();				
			} catch (Exception e) {}			
		}
		Date date = new Date();
		if (actDay == null) actDay = Integer.valueOf(date.getDay()).toString();
			
		out.append("<h1>" + actDay + "</h1>");
		if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) log.info(actDay);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();	
//		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//		com.google.appengine.api.datastore.Transaction txn = datastore.beginTransaction();

		try {

			out.append("<table>");
			
			Query queryParticipant = pm.newQuery("select from " + Participant.class.getName());
			@SuppressWarnings("unchecked")
			List<Participant> listParticipant = (List<Participant>) pm.newQuery(queryParticipant).execute();
			if (!listParticipant.isEmpty()) {				
				for (Participant l : listParticipant) {
					Player player = pm.getObjectById(Player.class, l.getPlayer());
					Match match = pm.getObjectById(Match.class, l.getMatch());
					if (actDay.equals(match.getDay())) {
						l.setPotential(null);		
						out.append("<tr><td>" + player.getName() + " - " + match.getDay() + "</td></tr>");
						if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) log.info(player.getName() + " - " + match.getDay());
					}
				}	
			}
			
			out.append("</table>");
			
			Integer count = new Integer(0);
			Query queryHistory = pm.newQuery("select from " + History.class.getName());
			queryHistory.setFilter("(date < pDate)");
			queryHistory.declareParameters("java.util.Date pDate");
			date.setTime(date.getTime() - Long.valueOf("2000000000"));
			@SuppressWarnings("unchecked")
			List<History> listHistory = (List<History>) pm.newQuery(queryHistory).execute(date);		
			out.append("<h1>" + date + "</h1>");
			if (!listHistory.isEmpty()) {
				for (History l : listHistory) {
//					pm.deletePersistent(l);
					count++ ;						 
				}	
			}

			out.append("<h1>" + count + "</h1>");
			if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) log.info(count.toString());
			
//			txn.commit();
			
		} catch (Exception e) {
			log.severe(e.getMessage()); 
		} finally {
//			if (txn.isActive()) {
//				 txn.rollback();
//			}
			pm.close();
		}			
	
		out.append("</body>");
		out.append("</html>");
		
		out.println();
		log.info(new Date() + " stop.");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
