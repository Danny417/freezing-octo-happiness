package com.google.guestbook;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class User {
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key userID;
	
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
	
	@Persistent (mappedBy = "guest")
	private List<Review> reviews;
	
	@Persistent
	private int rating;
	
	@Persistent (mappedBy = "host")
	private List<ParkingSpot> parkingSpots;
	
	public User(String fullName, Email mail){
		this.name = fullName;
		this.gmail = mail;
		this.parkingSpots = new ArrayList<ParkingSpot>();
	}
	
	// ACCESSORS

	public Key getUserID() {
		return userID;
	}

	public void setUserID(Key userID) {
		this.userID = userID;
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

	public List<ParkingSpot> getParkingSpots() {
		return parkingSpots;
	}

	public void setParkingSpots(List<ParkingSpot> parkingSpots) {
		this.parkingSpots = parkingSpots;
	}
	
}