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
	
	@Persistent 
	private ParkingSpotModel parkingSpot;
		
	@Persistent
	private UserModel user;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String reviewMessage;
	
	@Persistent
	private int rating;
	
	public ReviewModel(Date date, int rating, String reviewMessage){
		this.date = date;
		this.rating = rating;
		this.reviewMessage = reviewMessage;
	}

	public ParkingSpotModel getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpotModel ps) {
		this.parkingSpot = ps;
		this.parkingSpot.addReview(this);
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
	
	public UserModel getUser() {
		return this.user;
	}
	
	public void setUser(UserModel user) {
		this.user = user;
		user.addReview(this);
	}
}