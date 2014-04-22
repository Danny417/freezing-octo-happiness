package com.google.guestbook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserProfileServlet extends HttpServlet {
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		try {        	
			UserService userService = UserServiceFactory.getUserService();
			if(userService.getCurrentUser() == null) {
				resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
			}
			req.setAttribute("user", userService.getCurrentUser());
	        req.getRequestDispatcher("/WEB-INF/JSP/UserProfile.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} 
	}
}
