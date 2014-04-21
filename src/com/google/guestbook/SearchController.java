package com.google.guestbook;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class SearchController extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
    	//temp data to have some parking spots
		
	//Use URLFetchService to get image
	URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();
	HTTPResponse fetchResponse = fetchService.fetch(new URL(
            req.getParameter("url")));
	String 	fetchResponseContentType = null;
	for (HTTPHeader header : fetchResponse.getHeaders()) {
        // For each request header, check whether the name equals
        // "Content-Type"; if so, store the value of this header
        // in a member variable
        if (header.getName().equalsIgnoreCase("content-type")) {
            fetchResponseContentType = header.getValue();
            break;
        }
    }
	
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	ParkingSpotModel ps = new ParkingSpotModel(115.0, "2 address st.", 49.279387, -122.958822);
    
    	//Get the image with attribute "title"
    	if (fetchResponseContentType != null){
    		ImageModel image = new ImageModel();
    		image.setImageName(req.getParameter("title"));
            image.setImageType(fetchResponseContentType);

            // Set the image by passing in the bytes pulled
            // from the image fetched via the URL Fetch service
            image.setImage(fetchResponse.getContent());
            
            //Add to the ParkSpotModel for saving
            //test
            ps.setParkingImage(image);
    	}
    	
    	UserModel host = new UserModel("test user1", new Email("2testUser1@test.com"));
    	host.addParkingSpot(ps);
    	try {
        	pm.currentTransaction().begin();
    	    pm.makePersistent(host);
    	    pm.currentTransaction().commit();
    	} finally {
    	    if (pm.currentTransaction().isActive()) {
    	        pm.currentTransaction().rollback();
    	    }
    	}
        try {        	
            setReqAttr(req);
            //List<Entity> greetings = dataQueryRows("Greeting", guestbookKey);
	        //calculateRating(greetings);
	        req.getRequestDispatcher("/WEB-INF/JSP/Search.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} 
	}
	
	private void setReqAttr(HttpServletRequest req) {
    	UserService userService = UserServiceFactory.getUserService();
        req.setAttribute("user", userService.getCurrentUser());
        req.setAttribute("login", userService.createLoginURL(req.getRequestURI()));
        req.setAttribute("logout", userService.createLogoutURL(req.getRequestURI()));	
        String lat = req.getParameter("lat");	
        String lng = req.getParameter("lng");	
        if(lat != null && !lat.isEmpty() && lng != null && !lng.isEmpty()) {
        	req.setAttribute("parkingSpots", searchByCoord(lat, lng));
        }
    }
	
	//TODO implement where cause
	private List<ParkingSpotModel> searchByCoord(String lat, String lng) {
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	Query q = pm.newQuery(ParkingSpotModel.class);
    	List<ParkingSpotModel> results = null;
    	try {
    		results = (List<ParkingSpotModel>) q.execute();	
    	} finally {
    		q.closeAll();
    	}
    	return results;
	}
}
