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
        String num = req.getParameter("num");
        if(num != null && !num.isEmpty() && Integer.valueOf(num) != 0) {
        	int loop = Integer.valueOf(num);
        	List<Double> lats = new ArrayList<Double>();
        	List<Double> lngs = new ArrayList<Double>();
        	for(int i = 0; i < loop; i++) {
    	        String lat = req.getParameter("lat"+i);	
    	        String lng = req.getParameter("lng"+i);
    	        if(lat != null && !lat.isEmpty() && lng != null && !lng.isEmpty()) {
    	        	lats.add(Double.valueOf(lat));
    	        	lngs.add(Double.valueOf(lng));
    	        }    	        
        	}	
	        
	        req.setAttribute("parkingSpots", searchByCoord(lats, lngs));
        }
    }
	
	//GAE has some limitation of inequality queries
	//GeoHash is a better way to solve this problem but I do not have time to do that
	//so...Brutal force!
	private List<ParkingSpotModel> searchByCoord(List<Double> lats, List<Double> lngs) {
    	PersistenceManager pm = PMF.get().getPersistenceManager();
    	Query q = pm.newQuery(ParkingSpotModel.class);
    	List<ParkingSpotModel> finalRes = null;
    	try {
    		List<ParkingSpotModel> results = (List<ParkingSpotModel>) q.execute();	
    		finalRes = new ArrayList<ParkingSpotModel>();
    		for(ParkingSpotModel ps : results) {
    			for(int i = 0; i < lats.size(); i++) {
	        		if(ps.getLat() <= lats.get(i)+0.01 && ps.getLat() >= lats.get(i)-0.01 && ps.getLng() >= lngs.get(i)-0.01 && ps.getLng() <= lngs.get(i)+0.01) {
	        			finalRes.add(ps);	        			
	        		}
    			}
        	}
    	} catch(Exception e) {
    		System.out.println(e.toString());
    	} finally {
    		q.closeAll();
    	}
    	
    	return finalRes;
	}
}
