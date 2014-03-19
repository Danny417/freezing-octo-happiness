package com.google.guestbook;

import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.search.GeoPoint;

@PersistenceCapable
public class ParkingSpot{
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key parkingSpotID;
	
	@Persistent (dependent = "true")
	private User host; 
	
	@Persistent
	private String description;
	
	@Persistent
	private String address;
	
	@Persistent
	private float price;
	
	@Persistent
	private int rating;
	
	@Persistent (mappedBy = "parkingSpot")
	private List<Review> reviews;
	
	@Persistent
	private GeoPoint coordinate;
	
	@Persistent
	private AvailabilityManager availabilityManager;
	
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

	public void setHost(User host) {
		this.host = host;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCoordinate(GeoPoint coordinate) {
		this.coordinate = coordinate;
	}

	public ParkingSpot(User host, float newPrice, String address, float lat, float lng){
		this.host = host;
		this.price = newPrice;
		this.address = address;
		this.coordinate = new GeoPoint(Math.round(lat), Math.round(lng));
	}
	
	public Key getParkingSpotID() {
		return parkingSpotID;
	}

	public void setParkingSpotID(Key parkingSpotID) {
		this.parkingSpotID = parkingSpotID;
	}

	public User getHost() {
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

	public float getPrice() {
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

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public GeoPoint getCoordinate() {
		return coordinate;
	}
	
}