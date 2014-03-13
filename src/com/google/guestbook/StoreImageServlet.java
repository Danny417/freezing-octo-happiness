package com.google.guestbook;

import java.io.IOException;
import java.net.URL;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class StoreImageServlet extends HttpServlet {
	
	@Override 
	  public void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws IOException {
	        URLFetchService fetchService =
	            URLFetchServiceFactory.getURLFetchService();
	     // Fetch the image at the location given by the url query string parameter
	        HTTPResponse fetchResponse = fetchService.fetch(new URL(
	                req.getParameter("url")));
	        
	        String fetchResponseContentType = null;
	        for (HTTPHeader header : fetchResponse.getHeaders()) {
	            // For each request header, check whether the name equals
	            // "Content-Type"; if so, store the value of this header
	            // in a member variable
	            if (header.getName().equalsIgnoreCase("content-type")) {
	                fetchResponseContentType = header.getValue();
	                break;
	            }
	        }
	        
	        if (fetchResponseContentType != null) {
	            // Create a new image instance
	            ImageModel image = new ImageModel();
	            
	            //MAYBE NOT TITLE HERE!
	            image.setImageName(req.getParameter("title"));
	            image.setImageType(fetchResponseContentType);

	            // Set the image by passing in the bytes pulled
	            // from the image fetched via the URL Fetch service
	            image.setImage(fetchResponse.getContent());

	            //...

	            	///MAYBE WORKING!!!!!!!!!!!!!!!!!     
	            PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory();
	            PersistenceManager pm = pmf.getPersistenceManager();
	            try {
	                // Store the image in App Engine's datastore
	                pm.makePersistent(image);
	            } finally {
	                pm.close();
	            }
	        }
	}
}
