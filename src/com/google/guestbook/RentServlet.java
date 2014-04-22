package com.google.guestbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class RentServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		try {        	
            setReqAttr(req);
	        req.getRequestDispatcher("/WEB-INF/JSP/Rent.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} 
	}
	
	private void setReqAttr(HttpServletRequest req) {
    	UserService userService = UserServiceFactory.getUserService();
        req.setAttribute("user", userService.getCurrentUser());
        req.setAttribute("login", userService.createLoginURL(req.getRequestURI()));
        req.setAttribute("logout", userService.createLogoutURL(req.getRequestURI()));	
        //String num = req.getParameter("num");
        /*if(num != null && !num.isEmpty() && Integer.valueOf(num) != 0) {
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
        	}	*/
	}
}
