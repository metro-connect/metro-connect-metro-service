package com.metro.connect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metro.connect.dao.MetroDao;
import com.metro.connect.entity.Metro;

@Service
public class MetroServiceImpl implements MetroService {

	@Autowired
	private MetroDao metroDao;

	@Override
	public Metro addMetro(Metro metro) {
		return metroDao.save(metro);
	}

	@Override
	public Metro getMetroById(int metroId) {
		return metroDao.findById(metroId).get();
	}

	@Override
	public Metro updateMetro(Metro metro) {
		return metroDao.save(metro);
	}

	@Override
	public List<Metro> getAllMetro() {
		return metroDao.findAll();
	}

	@Override
	public List<Metro> getMetroByStatus(int metroStatus) {
		return metroDao.findByStatus(metroStatus);
	}

	@Override
	public List<Metro> getMetroByFromAndToLocation(int fromLocationId, int toLocationId, int status) {
		return metroDao.findByFromLocationIdAndToLocationIdAndStatus(fromLocationId, toLocationId, status);
	}

	@Override
	public Metro getMetroByNumber(String metroNumber) {
		return metroDao.findByNumber(metroNumber);
	}

	@Override
	public List<Metro> getAllMetrosByNumber(String metroNumber, int status) {
		return metroDao.findByNumberContainingIgnoreCaseAndStatus(metroNumber, status);
	}

}
