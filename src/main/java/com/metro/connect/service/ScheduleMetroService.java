package com.metro.connect.service;

import java.util.List;

import com.metro.connect.entity.ScheduleMetro;

public interface ScheduleMetroService {
	
	ScheduleMetro scheduleMetro(ScheduleMetro metro);
	List<ScheduleMetro> getByMetroId(int metroId);
	ScheduleMetro getByScheduleMetroId(int scheduleMetroId);
	ScheduleMetro getByScheduleMetroId(String scheduleMetroId);   // ref id
	List<ScheduleMetro> getByScheduleTimeBetweenAndStatus(String startDate, String endDate, int status);
	List<ScheduleMetro> getByScheduleTimeGreaterThanEqualAndStatus(String startDate, int status);
	List<ScheduleMetro> getByScheduleTimeGreaterThanEqual(String startDate);
	List<ScheduleMetro> getAllScheduledMetro();
	List<ScheduleMetro> getByScheduleTimeGreaterThanEqualAndStatusAndMetroId(String startDate, int status, int metroId);
	List<ScheduleMetro> getByScheduleTimeBetweenAndStatusAndMetroId(String startDate, String endDate , int status, int metroId);
	List<ScheduleMetro> getByScheduleTimeGreaterThanEqualAndMetroIdInAndStatus(String scheduleTime, List<Integer> metroIds, int status);
	List<ScheduleMetro> getByScheduleTimeBetweenAndStatusAndMetroIdIn(String startDate, String endDate , int status, List<Integer> metroIds);

}
