package com.google.guestbook;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;





public class ImageUploadController extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException{
		
		ServletFileUpload upload = new ServletFileUpload();
	    FileItemIterator iter;
		try {
			iter = upload.getItemIterator(req);
			FileItemStream imageItem = iter.next();
			InputStream imgStream = imageItem.openStream();
			Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
			
			//TODO do something after image is get
			//Can be either save to a ParkingModel or Save to datastore
			
			
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		
	    
	    
	}
	
}
