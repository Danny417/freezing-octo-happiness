package com.google.guestbook;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private int accuraccy;
	
	// TODO: we'll use Randy's data model here
	// the image is dependent
	// one to one
	
	// available time
	// TODO
	// add map<Date,List<Boolean>> yearmap ={}
	// add function to query which date is available
	private Map<Date, List<Boolean>> availability;

	public ParkingSpot(User host, float newPrice, String address, float lat, float lng){
		this.host = host;
		this.price = newPrice;
		this.address = address;
		this.coordinate = new GeoPoint(Math.round(lat), Math.round(lng));
		this.availability = new HashMap<Date, List<Boolean>>();
	}
	
	public boolean isAvailable(Date desiredDate){
		
		// check the date only (don't compare time yet)
		List<Boolean> dateStatus = availability.get(getZeroTimeDate(desiredDate));
		
		if (dateStatus == null){
			return false;
		} else {
			// if the date is available,
			// compare the time by getting the hour and minute
			// calculate the index using (total mins / 15)
			Calendar time = Calendar.getInstance();
			time.setTime(desiredDate);
			int hours = (time.get(Calendar.HOUR_OF_DAY) * 60) + time.get(Calendar.MINUTE);
			int index = (hours / 15);
			return dateStatus.get(index);	
		}
	}
	
	// creates DATE with 00:00:00 time, so we can compare both dates
	public static Date getZeroTimeDate(Date fecha) {
	    Date res = fecha;
	    Calendar calendar = Calendar.getInstance();

	    calendar.setTime( fecha );
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    res = calendar.getTime();

	    return res;
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

	public Map<Date, List<Boolean>> getAvailability() {
		return availability;
	}
	
}