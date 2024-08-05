package com.metro.connect.service;

import java.util.List;

import com.metro.connect.entity.MetroSeat;

public interface MetroSeatService {

	List<MetroSeat> addMetroSeats(List<MetroSeat> seats);

	List<MetroSeat> getMetroSeatByMetroId(int metroId);

}
