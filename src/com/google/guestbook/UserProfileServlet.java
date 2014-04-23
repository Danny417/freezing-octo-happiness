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
            
            List<RegisterParkingEntryModel> allregisterParkingEntries;
            Query allEntriesQuery = pm.newQuery(RegisterParkingEntryModel.class);
            allregisterParkingEntries = (List<RegisterParkingEntryModel>) allEntriesQuery.execute();
            System.out.println("number of registerParkingEntry = "+ allregisterParkingEntries.size());
            
            List<RegisterParkingEntryModel> relatedRegisterEntries = new ArrayList<RegisterParkingEntryModel>();
            List<ParkingSpotModel> rentParkingSpot = new ArrayList<ParkingSpotModel>();
            for (int i = 0; i < allregisterParkingEntries.size(); i++){
            	if (allregisterParkingEntries.get(i).getGuest().equals(host)){
            		relatedRegisterEntries.add(allregisterParkingEntries.get(i));
            		rentParkingSpot.add(allregisterParkingEntries.get(i).getParkingSpot());
            	}
            }
            req.setAttribute("relatedRegisterEntries", relatedRegisterEntries);
            req.setAttribute("rentParkingSpot", rentParkingSpot);
            System.out.println("number of rentParkingSpot = " + rentParkingSpot.size());
           
	        req.getRequestDispatcher("/WEB-INF/JSP/UserProfile.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} 
	}
}
