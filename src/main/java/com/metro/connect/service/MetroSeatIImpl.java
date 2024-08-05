package com.metro.connect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metro.connect.dao.MetroSeatDao;
import com.metro.connect.entity.MetroSeat;

@Service
public class MetroSeatIImpl implements MetroSeatService {
	
	@Autowired
	private MetroSeatDao metroSeatDao;

	@Override
	public List<MetroSeat> addMetroSeats(List<MetroSeat> seats) {
		return metroSeatDao.saveAll(seats);
	}

	@Override
	public List<MetroSeat> getMetroSeatByMetroId(int metroId) {
		return metroSeatDao.findByMetroId(metroId);
	}

}
