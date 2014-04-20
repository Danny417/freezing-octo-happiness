package com.google.guestbook;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class ReviewModel{
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent (dependent = "true")
	private ParkingSpotModel parkingSpot;
	
	@Persistent
	private UserModel guest;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String reviewMessage;
	
	@Persistent
	private int rating;
	
	public ReviewModel(UserModel user, Date date, ParkingSpotModel parkingSpot, int rating){
		this.guest = user;
		this.date = date;
		this.parkingSpot = parkingSpot;
		this.rating = rating;
	}

	public ParkingSpotModel getParkingSpot() {
		return parkingSpot;
	}

	public UserModel getUser() {
		return guest;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getReviewMessage() {
		return reviewMessage;
	}

	public void setReviewMessage(String reviewMessage) {
		this.reviewMessage = reviewMessage;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}