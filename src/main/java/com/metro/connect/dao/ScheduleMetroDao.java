package com.metro.connect.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.metro.connect.entity.ScheduleMetro;

@Repository
public interface ScheduleMetroDao extends JpaRepository<ScheduleMetro, Integer> {
	
	List<ScheduleMetro> findByMetroId(int metroId);
	ScheduleMetro findByScheduleMetroId(String scheduleMetroId);
	List<ScheduleMetro> findByScheduleTimeBetweenAndStatus(String startDate, String endDate, int status);
	List<ScheduleMetro> findByScheduleTimeGreaterThanEqualAndStatus(String startDate, int status);
	List<ScheduleMetro> findByScheduleTimeGreaterThanEqual(String startDate);
	List<ScheduleMetro> findByScheduleTimeGreaterThanEqualAndStatusAndMetroId(String startDate, int status, int metroId);
	List<ScheduleMetro> findByScheduleTimeBetweenAndStatusAndMetroId(String startDate, String endDate , int status, int metroId);
	@Query( "select st from ScheduleMetro st where scheduleTime >= :scheduleTime and metroId In (:metroIds) and status= :status")
	List<ScheduleMetro> findByScheduleTimeGreaterThanEqualAndMetroIdInAndStatus(@Param("scheduleTime") String scheduleTime, @Param("metroIds") List<Integer> metroIds, @Param("status") int status);
	List<ScheduleMetro> findByScheduleTimeBetweenAndStatusAndMetroIdIn(String startDate, String endDate, int status, List<Integer> metroIds);
}
