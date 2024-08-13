package com.metro.connect.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Metro {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private String number;
	private int totalCoach; // ToBeRemoved
	private int totalSeatInEachCoach; // ToBeRemoved
	private double seatPrice;
	private int fromLocationId;
	private int toLocationId;
	private int status;

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

	public int getFromLocationId() {
		return fromLocationId;
	}

	public void setFromLocationId(int fromLocationId) {
		this.fromLocationId = fromLocationId;
	}

	public int getToLocationId() {
		return toLocationId;
	}

	public void setToLocationId(int toLocationId) {
		this.toLocationId = toLocationId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Metro [id=" + id + ", name=" + name + ", number=" + number + ", totalCoach=" + totalCoach
				+ ", totalSeatInEachCoach=" + totalSeatInEachCoach + ", seatPrice=" + seatPrice + ", fromLocationId="
				+ fromLocationId + ", toLocationId=" + toLocationId + ", status=" + status + "]";
	}

	
}
