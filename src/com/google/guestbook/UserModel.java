package com.google.guestbook;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class UserModel {
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public enum Role {
		host, admin
	}
	
	@Persistent
	private Role role;
	
	@Persistent
	private String name;
	
	@Persistent
	private Email gmail;
	
	@Persistent
	private String phone;
	
	@Persistent
	private String licensePlate;
		
	@Persistent
	private int rating;
	
	@Persistent (mappedBy = "host")
	@Element(dependent = "true")
	private List<ParkingSpotModel> parkingSpots;
	
	@Persistent
	private List<ReviewModel> reviews;
	
	public UserModel(String fullName, Email mail){
		this.name = fullName;
		this.gmail = mail;
		this.key = KeyFactory.createKey(UserModel.class.getSimpleName(), mail.toString());
		this.parkingSpots = new ArrayList<ParkingSpotModel>();
		this.reviews = new ArrayList<ReviewModel>();
	}
	
	// ACCESSORS

	public Key getUserID() {
		return key;
	}

	public void setUserID(Key userID) {
		this.key = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Email getGmail() {
		return gmail;
	}

	public void setGmail(Email gmail) {
		this.gmail = gmail;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public List<ParkingSpotModel> getParkingSpots() {
		return parkingSpots;
	}

	public void setParkingSpots(List<ParkingSpotModel> parkingSpots) {
		this.parkingSpots = parkingSpots;
	}
	
	public void addParkingSpot(ParkingSpotModel ps) {
		if(this.parkingSpots == null) {
			this.parkingSpots = new ArrayList<ParkingSpotModel>();
		}
		if(!this.parkingSpots.contains(ps)) {
			this.parkingSpots.add(ps);
			ps.setHost(this);
		}
	}
	
	public void addReview(ReviewModel r) {
		if(this.reviews == null) {
			this.reviews = new ArrayList<ReviewModel>();
		}
		if(!this.reviews.contains(r)) {
			this.reviews.add(r);
			r.setUser(this);
		}
	}
	public static UserModel getUserById(Email mail, PersistenceManager pm) {
        Key userKey = KeyFactory.createKey(UserModel.class.getSimpleName(), mail.toString());
    	UserModel userModel = null;
    	try {
    		userModel =  pm.getObjectById(UserModel.class, userKey);    	
    	} catch (Exception e) {
    		System.out.println(e.toString());
    	}
    	return userModel;
	}
}