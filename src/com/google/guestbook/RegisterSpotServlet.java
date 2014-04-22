package com.google.guestbook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
      	
		//resp.sendRedirect("/reviewController/?markerID="+req.getParameter("markerID")); //Post/Redirect/Get design pattern
    }
	private void setReqAttr(HttpServletRequest req) {
        String spotId = req.getParameter("spotId");
        if(spotId != null && !spotId.isEmpty()) {
        	
        }        
	}
}
