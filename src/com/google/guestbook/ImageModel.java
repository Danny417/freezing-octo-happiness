package com.google.guestbook;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ImageModel {
	
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String ImageName;
	
	@Persistent
    @Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
    private String imageType;

	
	@Persistent 
	private Blob image;
	
	public Long getId(){
		return key.getId();
	}

	public String getImageName(){
		return ImageName;
	}
	
	public String getImageType(){
		return imageType;
	}
	
	public byte[] getImage(){
	      if (image == null) {
	            return null;
	        }

	        return image.getBytes();
	}
	
	public void setImageName(String Name){
		this.ImageName = Name;
	}
	
	public void setImageType(String imageType){
		this.imageType = imageType;
	}
	
	public void setImage(byte[] bytes){
		this.image = new Blob(bytes);
	}
	
}
