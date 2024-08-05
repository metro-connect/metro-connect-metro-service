package com.metro.connect.dto;



import java.util.List;

import com.metro.connect.entity.MetroSeat;

public class MetroDetail {

	private int id;
	private String name;
	private String number;
	private int totalCoach;
	private int totalSeatInEachCoach;
	private double seatPrice;
	private String fromLocation;
	private String toLocation;
	private List<MetroSeat> metroSeats;

	// for scheduled metro
	private int scheduleId;  // primary key for schedule metro
	private String scheduleMetroId; // unique ref id
	private String scheduleTime; // epochTime

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getTotalCoach() {
		return totalCoach;
	}

	public void setTotalCoach(int totalCoach) {
		this.totalCoach = totalCoach;
	}

	public int getTotalSeatInEachCoach() {
		return totalSeatInEachCoach;
	}

	public void setTotalSeatInEachCoach(int totalSeatInEachCoach) {
		this.totalSeatInEachCoach = totalSeatInEachCoach;
	}

	public double getSeatPrice() {
		return seatPrice;
	}

	public void setSeatPrice(double seatPrice) {
		this.seatPrice = seatPrice;
	}

	public String getFromLocation() {
		return fromLocation;
	}

	public void setFromLocation(String fromLocation) {
		this.fromLocation = fromLocation;
	}

	public String getToLocation() {
		return toLocation;
	}

	public void setToLocation(String toLocation) {
		this.toLocation = toLocation;
	}

	public String getScheduleMetroId() {
		return scheduleMetroId;
	}

	public void setScheduleMetroId(String scheduleMetroId) {
		this.scheduleMetroId = scheduleMetroId;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public List<MetroSeat> getMetroSeats() {
		return metroSeats;
	}

	public void setMetroSeats(List<MetroSeat> metroSeats) {
		this.metroSeats = metroSeats;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	
	

}
