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
import com.google.appengine.api.datastore.Text;
import com.google.gson.Gson;

@PersistenceCapable
public class AvailabilityManagerModel {
	
	@PrimaryKey
	@Persistent (valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key managerKey;
	
	@Persistent
	private Text availability;
	
	@Persistent
	private Boolean isNotAvailable;
	
	public AvailabilityManagerModel(){
		this.availability = new Text("{}");
		this.isNotAvailable = true;
	}
	
	public boolean isAvailable(Date desiredDate){
		Map<Date, List<Boolean>> result = new Gson().fromJson(this.availability.getValue(), HashMap.class);
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
		int startIndex = Integer.parseInt(startTime);
		int endIndex = Integer.parseInt(endTime);
		List<Boolean> timeSlots = new ArrayList<Boolean>();
		for(int i = 0; i < 48 ; i++){
			if(i >= startIndex && i < endIndex) {
				timeSlots.add(true);
			} else {
				timeSlots.add(false);
			}
		}
		Map<Date, List<Boolean>> result = new Gson().fromJson(this.availability.getValue(), HashMap.class);
		result.put(d, timeSlots);
		this.isNotAvailable = !timeSlots.contains(true);
		this.availability = new Text(new Gson().toJson(result));	
	}
	public void setAvaliableTime(Date d, String startTime, String endTime, Boolean isAvali) {
		Map<String, List<Boolean>> result = new Gson().fromJson(this.availability.getValue(), HashMap.class);
		int startIndex = Integer.parseInt(startTime);
		int endIndex = Integer.parseInt(endTime);
		for(int i = 0; i < 48 ; i++){
			if(i >= startIndex && i <= endIndex) {
				result.get(d.toString()).set(i, isAvali);
			} 
		}
		
		this.isNotAvailable = !result.get(d.toString()).contains(true);
		this.availability = new Text(new Gson().toJson(result));	
	}
	// ACCESSORS
	
	public Key getManagerKey() {
		return managerKey;
	}

	public void setManagerKey(Key managerKey) {
		this.managerKey = managerKey;
	}

	public Text getAvailability() {
		return availability;
	}

	public void setAvailability(Text availability) {
		this.availability = availability;
	}

	public Boolean isNotAvaliable() {
		return this.isNotAvailable;
	}
	
}