package com.google.guestbook;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class RegisterParkingEntryModel{
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key registerParkingEntryKey;
	
	@Persistent
	private UserModel guest;
	
	@Persistent
	private Date bookingDate;
	
	@Persistent
	private int price;
	
	@Persistent
	private ParkingSpotModel parkingSpot;
	
	public RegisterParkingEntryModel(Date bookingDate, int price, ParkingSpotModel parkingSpot, UserModel guest){
		this.price = price;
		this.bookingDate = bookingDate;
		this.parkingSpot = parkingSpot;
		this.guest = guest;
	}

	public Key getRegisterParkingEntryKey() {
		return registerParkingEntryKey;
	}

	public void setRegisterParkingEntryKey(Key registerParkingEntryKey) {
		this.registerParkingEntryKey = registerParkingEntryKey;
	}

	public UserModel getGuest() {
		return guest;
	}

	public void setGuest(UserModel guest) {
		this.guest = guest;
	}

	public Date getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public ParkingSpotModel getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpotModel parkingSpot) {
		this.parkingSpot = parkingSpot;
	}
	
	

}