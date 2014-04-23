package com.google.guestbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class UserProfileServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {   
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			
            req.setAttribute("user", userService.getCurrentUser());
            UserModel host = UserModel.getUserById(new Email(user.getEmail()), pm);
            List<ParkingSpotModel> ownedParkingSpot = host.getParkingSpots();
            req.setAttribute("ownedParkingSpot", ownedParkingSpot);
            System.out.println(ownedParkingSpot.size());
            
            List<RegisterParkingEntryModel> registerParkingEntries;
            Query allEntriesQuery = pm.newQuery(RegisterParkingEntryModel.class);
            registerParkingEntries = (List<RegisterParkingEntryModel>) allEntriesQuery.execute();
            System.out.println("number of registerParkingEntry = "+ registerParkingEntries.size());
            
            List<ParkingSpotModel> rentParkingSpot = new ArrayList<ParkingSpotModel>();
            for (int i = 0; i < registerParkingEntries.size(); i++){
            	if (registerParkingEntries.get(i).getGuest().getGmail() == (new Email(user.getEmail()))){
            		rentParkingSpot.add(registerParkingEntries.get(i).getParkingSpot());
            	}
            }
            System.out.println("number of rentParkingSpot = " + rentParkingSpot.size());
            
	        req.getRequestDispatcher("/WEB-INF/JSP/UserProfile.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} 
	}
	
	private void setReqAttr(HttpServletRequest req) {
    	UserService userService = UserServiceFactory.getUserService();
        req.setAttribute("user", userService.getCurrentUser());
        req.setAttribute("login", userService.createLoginURL(req.getRequestURI()));
        req.setAttribute("logout", userService.createLogoutURL(req.getRequestURI()));	
	}
}
