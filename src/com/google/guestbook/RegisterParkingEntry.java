package com.google.guestbook;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class RegisterParkingEntry{
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key registerParkingEntryKey;
	
	@Persistent
	private User guest;
	
	@Persistent
	private Date bookingDate;
	
	@Persistent
	private int price;
	
	@Persistent
	private ParkingSpot parkingSpot;
	
	public RegisterParkingEntry(Date bookingDate, int price, ParkingSpot parkingSpot, User guest){
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

	public User getGuest() {
		return guest;
	}

	public void setGuest(User guest) {
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

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}
	
	

}