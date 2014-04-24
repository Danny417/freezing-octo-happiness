package com.google.guestbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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

        	String id = req.getParameter("entryId");
           /* for (int i = 0; i < allregisterParkingEntries.size(); i++){
            	if (allregisterParkingEntries.get(i).getGuest().equals(host)){
            		if(id != null && !id.isEmpty()) {
                		if(id.equals(allregisterParkingEntries.get(i).getRegisterParkingEntryKey().toString())) {
                			pm.deletePersistent(allregisterParkingEntries.get(i));
                			continue;
                		}
                	}
            		relatedRegisterEntries.add(allregisterParkingEntries.get(i));
            	}
            }*/
            Iterator<RegisterParkingEntryModel> itr = allregisterParkingEntries.iterator();
            while (itr.hasNext()) {
            	RegisterParkingEntryModel entry = itr.next();
            	if (entry.getGuest().equals(host)){
            		if(id != null && !id.isEmpty()) {
                		if(id.equals(entry.getRegisterParkingEntryKey().toString())) {
                			pm.deletePersistent(entry);
                			continue;
                		}
                	}
            		relatedRegisterEntries.add(entry);
            	}
            }
            req.setAttribute("relatedRegisterEntries", relatedRegisterEntries);
           
	        req.getRequestDispatcher("/WEB-INF/JSP/UserProfile.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {	
		System.out.println("test");
		System.out.println(req.getParameter("entryId"));
		resp.sendRedirect("/UserProfile");
    }
}
