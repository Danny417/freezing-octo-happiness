package com.google.guestbook;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class ParkingSpotModel{
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key parkingSpotID;
	
	@Persistent
	private UserModel host; 
	
	@Persistent
	private String description;
	
	@Persistent
	private String address;
	
	@Persistent
	private double price;
	
	@Persistent
	private int rating;
	
	@Persistent (mappedBy = "parkingSpot")
	@Element(dependent = "true")
	private List<ReviewModel> reviews;
	
	@Persistent
	private double lat;
	
	@Persistent
	private double lng;
		
	@Persistent
	private int accuraccy; 	// range

	@Persistent
	private ImageModel parkingImage;
	
	public int getAccuraccy() {
		return accuraccy;
	}

	public void setAccuraccy(int accuraccy) {
		this.accuraccy = accuraccy;
	}

	public ImageModel getParkingImage() {
		return parkingImage;
	}

	public void setParkingImage(ImageModel parkingImage) {
		this.parkingImage = parkingImage;
	}

	public void setHost(UserModel host) {
		this.host = host;
		this.host.addParkingSpot(this);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public ParkingSpotModel(double newPrice, String address, double lat, double lng){
		this.price = newPrice;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		this.rating = 0;
		this.accuraccy = 0;
		this.reviews = new ArrayList<ReviewModel>();
		this.parkingSpotID = KeyFactory.createKey(ParkingSpotModel.class.getSimpleName(), address+String.valueOf(lat)+String.valueOf(lng));
	}
	
	public Key getParkingSpotID() {
		return parkingSpotID;
	}

	public void setParkingSpotID(Key parkingSpotID) {
		this.parkingSpotID = parkingSpotID;
	}

	public UserModel getHost() {
		return host;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public List<ReviewModel> getReviews() {
		return reviews;
	}

	public void addReview(ReviewModel review) {
		if(this.reviews == null) {
			this.reviews = new ArrayList<ReviewModel>();			
		}
		if(!this.reviews.contains(review)) {
			this.reviews.add(review);	
			review.setParkingSpot(this);		
		}
	}
	
	public double getLat() {
		return this.lat;
	}
	
	public double getLng() {
		return this.lng;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
		
	public static ParkingSpotModel getParkingSpotById(Key key, PersistenceManager pm) {
        ParkingSpotModel ps = null;
    	ps =  pm.getObjectById(ParkingSpotModel.class, key);
    	return ps;
	}
}