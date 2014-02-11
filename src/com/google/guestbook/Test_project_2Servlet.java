package com.google.guestbook;

import java.io.IOException;
import java.util.logging.Logger;

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

@SuppressWarnings("serial")
public class Test_project_2Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
	        try {
				String guestbookName = req.getParameter("guestbookName");
			    if (guestbookName == null) {
			        guestbookName = "default";
			    }
		        Key guestbookKey = KeyFactory.createKey("Guestbook", guestbookName);
	   		 	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
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
