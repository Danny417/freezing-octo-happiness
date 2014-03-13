package com.google.guestbook;

import java.io.IOException;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class GetImageServlet extends HttpServlet {
	 @Override
	    public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
		 
		    //MAYBE NOT TITLE HERE
	        String title = req.getParameter("title");
	        ImageModel image = getImage(title);

	        if (image != null && image.getImageType() != null &&
	                image.getImage() != null) {
	            // Set the appropriate Content-Type header and write the raw bytes
	            // to the response's output stream
	            resp.setContentType(image.getImageType());
	            resp.getOutputStream().write(image.getImage());
	        } else {
	            // If no image is found with the given title, redirect the user to
	            // a static image
	            resp.sendRedirect("/static/noimage.jpg");
	        }
	    }

	    //...
	 private ImageModel getImage(String title) {
		    PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory();
            PersistenceManager pm = pmf.getPersistenceManager();


		    // Search for any ImageModel object with the passed-in title; limit the number
		    // of results returned to 1 since there should be at most one Image with
		    // a given title
		    //NEED CHANGES!!!!!!!!!!!
		    Query query = pm.newQuery(ImageModel.class, "title == titleParam");
		    query.declareParameters("String titleParam");
		    query.setRange(0, 1);

		    try {
		        List<ImageModel> results = (List<ImageModel>) query.execute(title);
		        if (results.iterator().hasNext()) {
		            // If the results list is non-empty, return the first (and only)
		            // result
		            return results.get(0);
		        }
		    } finally {
		        query.closeAll();
		        pm.close();
		    }

		    return null;
		}
}
