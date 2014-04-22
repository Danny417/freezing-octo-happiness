package com.google.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.labs.repackaged.com.google.common.base.Splitter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.List;

public class ReviewController extends HttpServlet {
	
	 	@Override
	 	public void doGet(HttpServletRequest req, HttpServletResponse resp)
				throws IOException {	    
	        String markerID = req.getParameter("markerID");	                
	        PersistenceManager pm = PMF.get().getPersistenceManager();
	    	List<ReviewModel> reviews = null;
	    	try {
	    		javax.jdo.Query q = pm.newQuery(ReviewModel.class, "parkingSpot == parentKey");
	    		q.setOrdering("date desc");
	    		q.declareParameters(Key.class.getName()+" parentKey");
	    		reviews = (List<ReviewModel>)q.execute(getChildKeys(markerID));
	    		
	    	} finally {
	    		pm.close();
	    	}	    	
	        String responseHTMLString = new Gson().toJson(reviews);       
	        resp.setContentType("text/html");
	        resp.getWriter().println(responseHTMLString);        
		}    
	 		 	
	    @Override
	    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	                throws IOException {	        
	    	createReviewModel(req);
			resp.sendRedirect("/reviewController/?markerID="+req.getParameter("markerID")); //Post/Redirect/Get design pattern
	    }
	    
	    /*
	     * Create Greeting Type of Entity for Datastore
	     */
	    private void createReviewModel(HttpServletRequest req) {	  
	    	UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();

	        PersistenceManager pm = PMF.get().getPersistenceManager();
	        try {
		    	ParkingSpotModel ps = ParkingSpotModel.getParkingSpotById(getChildKeys(req.getParameter("markerID")), pm);
		        
		        SimpleDateFormat isoFormat = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss z");
		        isoFormat.setTimeZone(TimeZone.getTimeZone("PST"));
		        ReviewModel review = new ReviewModel(new Date(), Double.parseDouble(req.getParameter("rating")), req.getParameter("content"));
		        if(user != null) {
		        	UserModel userModel = UserModel.getUserById(new Email(user.getEmail()), pm);
		        	if(userModel == null) {
		        		userModel = new UserModel(user.getNickname(), new Email(user.getEmail()));
			        	pm.currentTransaction().begin();
			    	    pm.makePersistent(userModel);
			    	    pm.currentTransaction().commit();
		        	}
		        	review.setUser(userModel);
		        }
	        	ps.addReview(review);
	        	pm.currentTransaction().begin();
	    	    pm.makePersistent(ps);
	    	    pm.currentTransaction().commit();
	        } finally {
	        	if (pm.currentTransaction().isActive()) {
	    	        pm.currentTransaction().rollback();
	    	    }
	        }	
	        javax.jdo.Query q = pm.newQuery(UserModel.class);
	    	List<UserModel> users = null;
	    	try {
	    		users = (List<UserModel>) q.execute();	
	    	} finally {
	    		q.closeAll();
	    	}
	    	for(UserModel u : users) {
	    		System.out.println(u.getName()+" : "+u.getUserID().toString());
	    	}        
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
	    
	    private Key getChildKeys(String keyStr) {
	    	String[] pair = keyStr.split("/");
	    	String className = pair[0].substring(0, pair[0].indexOf("("));
	    	String keyval = pair[0].substring(pair[0].indexOf("(")+1, pair[0].indexOf(")"));
	    	Key result = null;
	    	try {
				Key parent = KeyFactory.createKey(Class.forName("com.google.guestbook."+className).getSimpleName(), keyval);
		    	className = pair[1].substring(0, pair[1].indexOf("("));
		    	keyval = pair[1].substring(pair[1].indexOf("(")+1, pair[1].indexOf(")"));
				result = KeyFactory.createKey(parent, Class.forName("com.google.guestbook."+className).getSimpleName(), keyval);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return result;
	    }
}
