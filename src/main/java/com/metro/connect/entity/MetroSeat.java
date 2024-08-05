package com.metro.connect.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MetroSeat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int metroId;

	private String seatNo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMetroId() {
		return metroId;
	}

	public void setMetroId(int metroId) {
		this.metroId = metroId;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}

	@Override
	public String toString() {
		return "MetroSeat [id=" + id + ", metroId=" + metroId + ", seatNo=" + seatNo + "]";
	}

}
