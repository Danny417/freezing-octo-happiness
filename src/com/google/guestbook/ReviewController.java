package com.google.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.com.google.common.base.Splitter;
import com.google.gson.Gson;

import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.List;

public class ReviewController extends HttpServlet {
	
	 	@Override
	 	public void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws IOException {	    
	        String reqMarkerID = req.getParameter("markerID");	                
	        Key guestbookKey = KeyFactory.createKey("Guestbook", reqMarkerID);
	        
	        setReqAttr(req);
	        List<Entity> greetings = dataQueryRows("Greeting", guestbookKey);
	        calculateRating(greetings);
	        String responseHTMLString = new Gson().toJson(greetings);       
	       
	        resp.setContentType("text/html");
	        resp.getWriter().println(responseHTMLString);        
		}    
	 		 	
	    @Override
	    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	                throws IOException {	        
	        String reqMarkerID = req.getParameter("markerID");
	        Key guestbookKey = KeyFactory.createKey("Guestbook", reqMarkerID);
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			datastore.put(createGreetingEntity("Greeting", guestbookKey, req));        
			resp.sendRedirect("/reviewController/?markerID="+reqMarkerID); //Post/Redirect/Get design pattern
	    }
	    
	    /*
	     * Create Greeting Type of Entity for Datastore
	     */
	    private Entity createGreetingEntity(String EntityKind, Key EntityKey, HttpServletRequest req) {	  
	    	UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();

	        Entity greeting = new Entity(EntityKind, EntityKey);
	        greeting.setProperty("user", (user == null) ? "anonymous person" : user.getNickname());
	        SimpleDateFormat isoFormat = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss z");
	        isoFormat.setTimeZone(TimeZone.getTimeZone("PST"));
	        greeting.setProperty("date", isoFormat.format(new Date()));	        
	        greeting.setProperty("content", req.getParameter("content"));
	        greeting.setProperty("markerID", req.getParameter("markerID"));
	        greeting.setProperty("rating", req.getParameter("rating"));
	        greeting.setProperty("index", (int) (new Date().getTime()/1000));
	    	return greeting;
	    }
	    
	    /*
	     * Query max 99 rows of data by given Entity type,key, markerId
	     * return sorted list
	     */
	    private List<Entity> dataQueryRows(String EntityKind, Key EntityKey) {
	    	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            // Run an ancestor query to ensure we see the most up-to-date
            // view of the Greetings belonging to the selected Guestbook.
            Query query = new Query(EntityKind, EntityKey).addSort("index", Query.SortDirection.DESCENDING);
            return datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));    	
	    }
	    
	    private void calculateRating(List<Entity> entity){
	    	float averageRank = 0;
	    	for (Entity e : entity) {
	    		System.out.println("rating of greeting is " + e.getProperty("rating"));
	    		averageRank = averageRank + Float.parseFloat(""+e.getProperty("rating"));
	    	}
	    	averageRank = averageRank / entity.size();
	    	System.out.println(averageRank);
	    	for (Entity e: entity){
	    		e.setProperty("totalRank", averageRank);
	    	}
	    }
	    
	    /*
	     * Set request's attributes to pass greetings to JSP
	     */
	    private void setReqAttr(HttpServletRequest req) {
	    	UserService userService = UserServiceFactory.getUserService();
	       // req.setAttribute("guestbookName", guestbookName);
	       // req.setAttribute("greetings", ent);
	        req.setAttribute("user", userService.getCurrentUser());
	        req.setAttribute("login", userService.createLoginURL(req.getRequestURI()));
	        req.setAttribute("logout", userService.createLogoutURL(req.getRequestURI()));	
	       // req.setAttribute("guestbookMsg", (ent.size() == 0) ? "Guestbook '"+guestbookName+"' has no message": "Recent 10 messages in Guestbook '"+guestbookName+"'");
	    }
}
