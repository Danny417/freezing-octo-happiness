package com.google.guestbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class SearchController extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
    	
		//temp data to have some parking spots
    	ParkingSpotModel ps = new ParkingSpotModel(115.0, "3 address st.", 49.279387, -122.958822);
    	UserModel host = new UserModel("test user32", new Email("2testUser1@test.com"));
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
	
	//GAE has some limitation of inequality queries
	//GeoHash is a better way to solve this problem but I do not have time to do that
	//so...Brutal force!
	private List<ParkingSpotModel> searchByCoord(String lat, String lng) {
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	Double maxlat = Double.valueOf(lat)+0.01;
    	Double maxlng = Double.valueOf(lng)+0.01; //~1.1km range
    	Double minlat = Double.valueOf(lat)-0.01;
    	Double minlng = Double.valueOf(lng)-0.01;
    	Query q = pm.newQuery(ParkingSpotModel.class, "lat <= maxlat");
    	q.declareParameters("java.lang.Double maxlat");
    	List<ParkingSpotModel> results = null;
    	try {
    		results = (List<ParkingSpotModel>) q.execute(maxlat);	
    		List<ParkingSpotModel> removeList = new ArrayList<ParkingSpotModel>();
    		for(ParkingSpotModel ps : results) {
        		if(ps.getLat() < minlat || ps.getLng() < minlng || ps.getLng() > maxlng) {
        			removeList.add(ps);
        		}
        	}
    		for(ParkingSpotModel ps : removeList) {
    			results.remove(ps);
    		}
    	} catch(Exception e) {
    		System.out.println(e.toString());
    	} finally {
    		q.closeAll();
    	}
    	
    	return results;
	}
}
