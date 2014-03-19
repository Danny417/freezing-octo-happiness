package com.google.guestbook;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class AvailabilityManager {
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key managerKey;
	
	@Persistent (dependent = "true")
	private ParkingSpot parkingSpot;

	@Persistent
	private Map<Date, List<Boolean>> availability;
	
	public AvailabilityManager(ParkingSpot pSpot, Map<Date, List<Boolean>> tSlots){
		this.parkingSpot = pSpot;
		this.availability = tSlots;
	}
	
	public boolean isAvailable(Date desiredDate){
		
		// check the date only (don't compare time yet)
		List<Boolean> dateStatus = availability.get(getZeroTimeDate(desiredDate));
		
		if (dateStatus == null){
			return false;
		} else {
			// if the date is available,
			// compare the time by getting the hour and minute
			// calculate the index using (total mins / 30)
			Calendar time = Calendar.getInstance();
			time.setTime(desiredDate);
			int hours = (time.get(Calendar.HOUR_OF_DAY) * 60) + time.get(Calendar.MINUTE);
			int index = (hours / 30);
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
	
	// ACCESSORS
	
	public Key getManagerKey() {
		return managerKey;
	}

	public void setManagerKey(Key managerKey) {
		this.managerKey = managerKey;
	}

	public ParkingSpot getParkingSpot() {
		return parkingSpot;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	public Map<Date, List<Boolean>> getAvailability() {
		return availability;
	}

	public void setAvailability(Map<Date, List<Boolean>> availability) {
		this.availability = availability;
	}

	
}