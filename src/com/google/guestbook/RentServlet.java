package com.google.guestbook;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	            System.out.println(ps.getAddress());
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
		resp.sendRedirect("/UserProfile?userId="); 
	}
}
