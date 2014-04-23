package com.google.guestbook;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class RentServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {       
			UserService userService = UserServiceFactory.getUserService();
	    	if(userService.getCurrentUser() == null) {
		        resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	    	}
	        req.setAttribute("user", userService.getCurrentUser());
			String parkingID = req.getParameter("parkingID");
			if(parkingID != null && !parkingID.isEmpty()) {
				ParkingSpotModel ps = ParkingSpotModel.getParkingSpotById(ParkingSpotModel.getChildKeys(parkingID), pm);
	            req.setAttribute("parkingSpot", ps);
	            req.setAttribute("avaliability", ps.getAvailability().getAvailability().getValue());
	            req.setAttribute("parkingID", parkingID);
			}
	        req.getRequestDispatcher("/WEB-INF/JSP/Rent.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} finally {
        	if (pm.currentTransaction().isActive()) {
    	        pm.currentTransaction().rollback();
    	    }
        }	
	}	
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {	
		UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			UserModel client = UserModel.getUserById(new Email(user.getEmail()), pm);
        	if(client == null) {
        		client = new UserModel(user.getNickname(), new Email(user.getEmail()));
	        	pm.currentTransaction().begin();
	    	    pm.makePersistent(client);
	    	    pm.currentTransaction().commit();
	    	    pm.flush();
        	}
			String parkingID = req.getParameter("parkingID");
			ParkingSpotModel ps = ParkingSpotModel.getParkingSpotById(ParkingSpotModel.getChildKeys(parkingID), pm);
			Date d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(req.getParameter("date"));	
			String startIndex =req.getParameter("startTime");
			String endIndex = req.getParameter("endTime");
			ps.getAvailability().setAvaliableTime(d, startIndex, endIndex, false);
			double price = ps.getPrice()*(Integer.parseInt(endIndex)-Integer.parseInt(startIndex))/2;
			RegisterParkingEntryModel entry = new RegisterParkingEntryModel(d, price, ps, client);
			pm.currentTransaction().begin();
    	    pm.makePersistent(ps);
    	    pm.makePersistent(entry);    	    
    	    pm.currentTransaction().commit();
    	    pm.flush();
    	    Query q = pm.newQuery(ParkingSpotModel.class);
    	    List<ParkingSpotModel> results = (List<ParkingSpotModel>) q.execute();
    	    for(ParkingSpotModel p : results) {
    	    	System.out.println(p.getAvailability().getAvailability().getValue());
    	    }
    	    q = pm.newQuery(RegisterParkingEntryModel.class);
    	    List<RegisterParkingEntryModel> results2 = (List<RegisterParkingEntryModel>) q.execute();
    	    for(RegisterParkingEntryModel p : results2) {
    	    	System.out.println("test");
    	    }
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} finally {
        	if (pm.currentTransaction().isActive()) {
    	        pm.currentTransaction().rollback();
    	    }
        }	
		resp.sendRedirect("/UserProfile"); 
	}
}
