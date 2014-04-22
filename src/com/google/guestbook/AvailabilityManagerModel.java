package com.google.guestbook;

import java.util.ArrayList;
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
import com.google.gson.Gson;

@PersistenceCapable
public class AvailabilityManagerModel {
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key managerKey;
	
	@Persistent
	private String availability;
	
	public AvailabilityManagerModel(){
		this.availability = "{}";
	}
	
	public boolean isAvailable(Date desiredDate){
		Map<Date, List<Boolean>> result = new Gson().fromJson(this.availability, HashMap.class);
		// check the date only (don't compare time yet)
		List<Boolean> dateStatus = result.get(getZeroTimeDate(desiredDate));
		
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
	
	public void addAvaliableTime(Date d, String startTime, String endTime) {
		String[] hhmm = startTime.split(":");
		int minutes = Integer.parseInt(hhmm[0])*60+Integer.parseInt(hhmm[1]);
		int startIndex = minutes/30;
		hhmm = endTime.split(":");
		minutes = Integer.parseInt(hhmm[0])*60+Integer.parseInt(hhmm[1]);
		int endIndex = minutes/30;
		List<Boolean> timeSlots = new ArrayList<Boolean>();
		for(int i = 0; i < 48 ; i++){
			if(i >= startIndex && i <= endIndex) {
				timeSlots.add(true);
			} else {
				timeSlots.add(false);
			}
		}
		Map<Date, List<Boolean>> result = new Gson().fromJson(this.availability, HashMap.class);
		result.put(d, timeSlots);
		this.availability = new Gson().toJson(result);	
	}
	// ACCESSORS
	
	public Key getManagerKey() {
		return managerKey;
	}

	public void setManagerKey(Key managerKey) {
		this.managerKey = managerKey;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	
}