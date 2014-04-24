package com.google.guestbook;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class MainServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        try {
            setReqAttr(req);
	        req.getRequestDispatcher("/WEB-INF/JSP/Main.jsp").forward(req, resp);
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
