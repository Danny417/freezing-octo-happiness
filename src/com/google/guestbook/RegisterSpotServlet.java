package com.google.guestbook;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class RegisterSpotServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		try {        	
	    	UserService userService = UserServiceFactory.getUserService();
	    	if(userService.getCurrentUser() == null) {
		        resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	    	}
	        req.setAttribute("user", userService.getCurrentUser());
            setReqAttr(req);
	        req.getRequestDispatcher("/WEB-INF/JSP/RegisterSpot.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {	  
    	UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        PersistenceManager pm = PMF.get().getPersistenceManager();
		try {        
			double price = Double.valueOf(req.getParameter("price"));
			ParkingSpotModel ps = new ParkingSpotModel(price, req.getParameter("address"), Double.valueOf(req.getParameter("lat")),Double.valueOf(req.getParameter("lng")));
			String desc = req.getParameter("desc");
			if(desc != null && !desc.isEmpty()) {
				ps.setDescription(desc);
			}
			AvailabilityManagerModel avail = new AvailabilityManagerModel();
			Date d;
			d = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).parse(req.getParameter("date"));		
			avail.addAvaliableTime(d, req.getParameter("startTime"), req.getParameter("endTime"));
			ps.setAvailability(avail);
			UserModel host = UserModel.getUserById(new Email(user.getEmail()), pm);
        	if(host == null) {
        		host = new UserModel(user.getNickname(), new Email(user.getEmail()));
	        	pm.currentTransaction().begin();
	    	    pm.makePersistent(host);
	    	    pm.currentTransaction().commit();
	    	    pm.flush();
        	}
        	
        	host.addParkingSpot(ps);
        	pm.currentTransaction().begin();
    	    pm.makePersistent(host);
    	    pm.currentTransaction().commit();
    	    Query q = pm.newQuery(ParkingSpotModel.class);
    	    List<ParkingSpotModel> results = (List<ParkingSpotModel>) q.execute();
    	    for(ParkingSpotModel p : results) {
    	    	System.out.println(p.getAvailability().getAvailability());
    	    }
    	    q = pm.newQuery(AvailabilityManagerModel.class);
    	    List<AvailabilityManagerModel> results2 = (List<AvailabilityManagerModel>) q.execute();
    	    for(AvailabilityManagerModel p : results2) {
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
		
		resp.sendRedirect("/"); 
	}
	
	private void setReqAttr(HttpServletRequest req) {
        String spotId = req.getParameter("spotId");
        if(spotId != null && !spotId.isEmpty()) {
        	
        }        
	}
}
