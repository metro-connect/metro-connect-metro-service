package com.metro.connect.service;

import java.util.List;

import com.metro.connect.entity.Metro;

public interface MetroService {
	
	Metro addMetro(Metro metro);
	Metro getMetroById(int metroId);
	Metro updateMetro(Metro metro);
	List<Metro> getAllMetro();
	List<Metro> getMetroByStatus(int metroStatus);
	List<Metro> getMetroByFromAndToLocation(int fromLocationId, int toLocationId, int status);
	Metro getMetroByNumber(String metroNumber);
	List<Metro> getAllMetrosByNumber(String metroNumber, int status);
	

}
