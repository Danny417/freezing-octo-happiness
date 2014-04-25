package com.google.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.gson.Gson;

import java.util.Date;
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
	    		reviews = (List<ReviewModel>)q.execute(ParkingSpotModel.getChildKeys(markerID));
	    		
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
		    	ParkingSpotModel ps = ParkingSpotModel.getParkingSpotById(ParkingSpotModel.getChildKeys(req.getParameter("markerID")), pm);
		        
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
	        } catch (Exception e) {
	        	System.out.println(e.toString());
	        }
	        finally {
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
}
