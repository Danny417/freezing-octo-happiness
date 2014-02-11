package com.google.guestbook;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.util.Date;

import com.google.appengine.api.datastore.Query;

import java.util.List;

public class SignGuestbookServlet extends HttpServlet {
	 private static final Logger log = Logger.getLogger(SignGuestbookServlet.class.getName());

	    @Override
	    public void doPost(HttpServletRequest req, HttpServletResponse resp)
	                throws IOException {
	    	UserService userService = UserServiceFactory.getUserService();
	        User user = userService.getCurrentUser();

	        // We have one entity group per Guestbook with all Greetings residing
	        // in the same entity group as the Guestbook to which they belong.
	        // This lets us run a transactional ancestor query to retrieve all
	        // Greetings for a given Guestbook.  However, the write rate to each
	        // Guestbook should be limited to ~1/second.
	        String guestbookName = req.getParameter("guestbookName");
	        Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
	        String content = req.getParameter("content");
	        Date date = new Date();
	        Entity greeting = new Entity("Greeting", guestbookKey);
	        greeting.setProperty("user", user);
	        greeting.setProperty("date", date);
	        greeting.setProperty("content", content);
	        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	        datastore.put(greeting);
	        try {
	            // Run an ancestor query to ensure we see the most up-to-date
	            // view of the Greetings belonging to the selected Guestbook.
	            Query query = new Query("Greeting", guestbookKey).addSort("date", Query.SortDirection.DESCENDING);
	            List<Entity> greetings = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));
		        req.setAttribute("guestbookName", guestbookName);
		        req.setAttribute("greetings", greetings);
		        req.getRequestDispatcher("/WEB-INF/JSP/guestbook.jsp").forward(req, resp);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
}
