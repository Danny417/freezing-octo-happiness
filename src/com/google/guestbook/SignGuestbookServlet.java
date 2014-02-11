package com.google.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;
import java.util.TimeZone;

import com.google.appengine.api.datastore.Query;

import java.util.List;

public class SignGuestbookServlet extends HttpServlet {
	
	 	@Override
	 	public void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws IOException {
			String guestbookName = req.getParameter("guestbookName");
		    if (guestbookName == null) {
		        guestbookName = "default";
		    }
	        Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
	        
	        try {
	            setReqAttr(req, guestbookName, dataQuery10Rows("Greeting", guestbookKey));
		        req.getRequestDispatcher("/WEB-INF/JSP/guestbook.jsp").forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 	
	    @Override
	    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	                throws IOException {	        
	        String guestbookName = req.getParameter("guestbookName");
	        Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
	        
	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			datastore.put(createGreetingEntity("Greeting", guestbookKey, req));
			resp.sendRedirect("/"); //Post/Redirect/Get design pattern
	    }
	    
	    /*
	     * Create Greeting Type of Entity for Datastore
	     */
	    private Entity createGreetingEntity(String EntityKind, Key EntityKey, HttpServletRequest req) {	  
	    	UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();
	        
	        Entity greeting = new Entity(EntityKind, EntityKey);
	        
	        greeting.setProperty("user", (user == null) ? "anonymous person" : user.getNickname());
	        SimpleDateFormat isoFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
	        isoFormat.setTimeZone(TimeZone.getTimeZone("PST"));
	        greeting.setProperty("date", isoFormat.format(new Date()));	        
	        greeting.setProperty("content", req.getParameter("content"));
	    	return greeting;
	    }
	    
	    /*
	     * Query 10 rows of data by given Entity type and key
	     * return sorted list
	     */
	    private List<Entity> dataQuery10Rows(String EntityKind, Key EntityKey) {
	    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            // Run an ancestor query to ensure we see the most up-to-date
            // view of the Greetings belonging to the selected Guestbook.
            Query query = new Query(EntityKind, EntityKey).addSort("date", Query.SortDirection.DESCENDING);
            return datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));    	
	    }
	    
	    /*
	     * Set request's attributes to pass greetings to JSP
	     */
	    private void setReqAttr(HttpServletRequest req, String guestbookName, List<Entity> ent) {
	    	UserService userService = UserServiceFactory.getUserService();
	        req.setAttribute("guestbookName", guestbookName);
	        req.setAttribute("greetings", ent);
	        req.setAttribute("user", userService.getCurrentUser());
	        req.setAttribute("login", userService.createLoginURL(req.getRequestURI()));
	        req.setAttribute("logout", userService.createLogoutURL(req.getRequestURI()));	
	        req.setAttribute("guestbookMsg", (ent.size() == 0) ? "Guestbook '"+guestbookName+"' has no message": "Messages in Guestbook '"+guestbookName+"'");
	    }
}
