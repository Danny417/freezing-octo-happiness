package com.google.guestbook;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable
public class RegisterParkingEntryModel{
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key registerParkingEntryKey;
	
	@Persistent
	@Unowned
	private UserModel guest;
	
	@Persistent
	private Date bookingDate;
	
	@Persistent
	private double price;
	
	@Persistent
	@Unowned
	private ParkingSpotModel parkingSpot;
	
	public RegisterParkingEntryModel(Date bookingDate, double price, ParkingSpotModel parkingSpot, UserModel guest, String startTime, String endTime){
		this.price = price;
		this.bookingDate = bookingDate;
		this.parkingSpot = parkingSpot;
		this.guest = guest;
		this.registerParkingEntryKey = KeyFactory.createKey(RegisterParkingEntryModel.class.getSimpleName(), bookingDate.toString()+startTime+endTime+parkingSpot.toString()+guest.toString());
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

	public double getPrice() {
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