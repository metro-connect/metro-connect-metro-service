package com.metro.connect.dto;



import java.util.List;

import com.metro.connect.entity.MetroSeat;

public class AddScheduleMetroTicketRequestDto {
	
	private int scheduleMetroId;
	
	private List<MetroSeat> metroSeats;

	public int getScheduleMetroId() {
		return scheduleMetroId;
	}

	public void setScheduleMetroId(int scheduleMetroId) {
		this.scheduleMetroId = scheduleMetroId;
	}

	public List<MetroSeat> getMetroSeats() {
		return metroSeats;
	}

	public void setMetroSeats(List<MetroSeat> metroSeats) {
		this.metroSeats = metroSeats;
	}
	
	

}
