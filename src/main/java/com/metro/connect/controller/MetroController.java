package com.metro.connect.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.metro.connect.dto.CommonApiResponse;
import com.metro.connect.dto.MetroDetailResponseDto;
import com.metro.connect.dto.ScheduleMetroResponseDto;
import com.metro.connect.entity.Metro;
import com.metro.connect.entity.ScheduleMetro;
import com.metro.connect.resource.MetroResource;

@RestController
@RequestMapping("/api/metro/")
@CrossOrigin(origins = "http://localhost:3000")
public class MetroController {

	@Autowired
	private MetroResource metroResource;

	@PostMapping("/register")
	public ResponseEntity<CommonApiResponse> registerMetro(@RequestBody Metro metro) {
		return metroResource.addMetro(metro);
	}

	@PutMapping("/update")
	public ResponseEntity<CommonApiResponse> updateMetro(@RequestBody Metro metro) {
		return metroResource.addMetro(metro);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<CommonApiResponse> deleteMetro(@RequestParam("metroId") int metroId) {
		return metroResource.deleteMetro(metroId);
	}

	@GetMapping("/fetch")
	public ResponseEntity<MetroDetailResponseDto> fetchMetroDetail(@RequestParam("metroId") int metroId) {
		return metroResource.fetchMetroDetail(metroId);
	}
	
	@GetMapping("/fetch/all")
	public ResponseEntity<MetroDetailResponseDto> fetchAllMetros() {
		return metroResource.fetchAllMetros();
	}

	@GetMapping("/fetch/location/search")
	public ResponseEntity<MetroDetailResponseDto> fetchMetroDetailsFromAndToLocation(
			@RequestParam("fromLocationId") int fromLocationId, @RequestParam("toLocationId") int toLocationId) {
		return metroResource.fetchMetroDetailsFromAndToLocation(fromLocationId, toLocationId);
	}

	@GetMapping("/search/metroNumber")
	public ResponseEntity<MetroDetailResponseDto> searchMetro(@RequestParam("metroNo") String metroNumber) {
		return metroResource.searchMetro(metroNumber);
	}

	@PostMapping("/schedule")
	public ResponseEntity<CommonApiResponse> scheduleMetro(@RequestBody ScheduleMetro scheduleMetro) {
		return metroResource.scheduleMetro(scheduleMetro);
	}

	@GetMapping("/schedule/fetch/id")
	public ResponseEntity<ScheduleMetroResponseDto> fetchScheduledMetroById(
			@RequestParam("scheduleMetroId") int scheduleMetroId) {
		return metroResource.getScheduledMetroById(scheduleMetroId);
	}

	@DeleteMapping("/schedule")
	public ResponseEntity<CommonApiResponse> deleteScheduledMetro(
			@RequestParam("scheduleMetroId") String scheduleMetroId) {
		return metroResource.deleteScheduleMetro(scheduleMetroId);
	}

	// search scheduled metros by using time range
	// for all metro
	@GetMapping("/scheduled/search/metro/all/time")
	public ResponseEntity<ScheduleMetroResponseDto> searchScheduleMetro(@RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime) {
		return metroResource.searchScheduledMetroByTime(startTime, endTime);
	}

	// search scheduled metros from current time
	// for all metro
	@GetMapping("/scheduled/metros/all")
	public ResponseEntity<ScheduleMetroResponseDto> fecthedScheduledMetro() {
		return metroResource.fetchedScheduledMetro();
	}

	// search scheduled metro from current time
	// for single metro
	@GetMapping("/search/metro/time")
	public ResponseEntity<ScheduleMetroResponseDto> fetchScheduledMetroByTime(
			@RequestParam("startTime") String startTime, @RequestParam("metroId") int metroId) {
		return metroResource.fetchScheduledMetroByTime(startTime, metroId);
	}

	// search scheduled metro by using time range
	// for single metro
	@GetMapping("/search/metro/time/range")
	public ResponseEntity<ScheduleMetroResponseDto> fetchScheduledMetroByStartEndTime(
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam("metroId") int metroId) {
		return metroResource.fetchScheduledMetroByTimeRange(startTime, endTime, metroId);
	}

	// search scheduled metro by source and destination location
	@GetMapping("/search/metro/location")
	public ResponseEntity<ScheduleMetroResponseDto> fetchScheduledMetroByLocations(
			@RequestParam("sourceLocationId") int sourceLocationId, @RequestParam("destinationLocationId") int destinationLocationId) {
		return metroResource.fetchScheduledMetrosByLocationsAndCurrentTime(sourceLocationId, destinationLocationId);
	}
	
	@GetMapping("/serach/scheduled/metrod")
	public ResponseEntity<ScheduleMetroResponseDto> searchScheduledMetro(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("sourceLocationId") int sourceLocationId, @RequestParam("destinationLocationId") int destinationLocationId) {
		return metroResource.searchScheduledMetro(startDate, endDate, sourceLocationId, destinationLocationId);
	}

}
