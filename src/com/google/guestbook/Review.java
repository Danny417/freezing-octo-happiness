package com.google.guestbook;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Review{
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent (dependent = "true")
	private ParkingSpot parkingSpot;
	
	@Persistent
	private User guest;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String reviewMessage;
	
	@Persistent
	private int rating;
	
	public Review(User user, Date date, ParkingSpot parkingSpot, int rating){
		this.guest = user;
		this.date = date;
		this.parkingSpot = parkingSpot;
		this.rating = rating;
	}

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public User getUser() {
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