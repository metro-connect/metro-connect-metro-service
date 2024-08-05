package com.metro.connect.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metro.connect.dao.ScheduleMetroDao;
import com.metro.connect.entity.ScheduleMetro;

@Service
public class ScheduleMetroServiceImpl implements ScheduleMetroService {

	@Autowired
	private ScheduleMetroDao scheduleMetroDao;

	@Override
	public ScheduleMetro scheduleMetro(ScheduleMetro metro) {
		return scheduleMetroDao.save(metro);
	}

	@Override
	public List<ScheduleMetro> getByMetroId(int metroId) {
		return scheduleMetroDao.findByMetroId(metroId);
	}

	@Override
	public ScheduleMetro getByScheduleMetroId(String scheduleMetroId) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findByScheduleMetroId(scheduleMetroId);
	}

	@Override
	public List<ScheduleMetro> getByScheduleTimeBetweenAndStatus(String startDate, String endDate, int status) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findByScheduleTimeBetweenAndStatus(startDate, endDate, status);
	}

	@Override
	public List<ScheduleMetro> getByScheduleTimeGreaterThanEqualAndStatus(String startDate, int status) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findByScheduleTimeGreaterThanEqualAndStatus(startDate, status);
	}

	@Override
	public List<ScheduleMetro> getByScheduleTimeGreaterThanEqual(String startDate) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findByScheduleTimeGreaterThanEqual(startDate);
	}

	@Override
	public List<ScheduleMetro> getAllScheduledMetro() {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findAll();
	}

	@Override
	public List<ScheduleMetro> getByScheduleTimeGreaterThanEqualAndStatusAndMetroId(String startDate, int status,
			int metroId) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findByScheduleTimeGreaterThanEqualAndStatusAndMetroId(startDate, status, metroId);
	}

	@Override
	public List<ScheduleMetro> getByScheduleTimeBetweenAndStatusAndMetroId(String startDate, String endDate, int status,
			int metroId) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findByScheduleTimeBetweenAndStatusAndMetroId(startDate, endDate, status, metroId);
	}

	@Override
	public ScheduleMetro getByScheduleMetroId(int scheduleMetroId) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findById(scheduleMetroId).get();
	}

	@Override
	public List<ScheduleMetro> getByScheduleTimeGreaterThanEqualAndMetroIdInAndStatus(String scheduleTime,
			List<Integer> metroIds, int status) {
		return scheduleMetroDao.findByScheduleTimeGreaterThanEqualAndMetroIdInAndStatus(scheduleTime, metroIds, status);
	}

	@Override
	public List<ScheduleMetro> getByScheduleTimeBetweenAndStatusAndMetroIdIn(String startDate, String endDate,
			int status, List<Integer> metroIds) {
		// TODO Auto-generated method stub
		return scheduleMetroDao.findByScheduleTimeBetweenAndStatusAndMetroIdIn(startDate, endDate, status, metroIds);
	}

}
