package com.metro.connect.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ScheduleMetro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int metroId;

	private String scheduleMetroId; // unique ref id

	private String scheduleTime; // epochTime

	private int status;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
