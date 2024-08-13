package com.metro.connect.resource;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import com.metro.connect.controller.MetroController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metro.connect.constants.DatabaseConstant.MetroStatus;
import com.metro.connect.dto.AddScheduleMetroTicketRequestDto;
import com.metro.connect.dto.CommonApiResponse;
import com.metro.connect.dto.LocationResponseDto;
import com.metro.connect.dto.MetroDetail;
import com.metro.connect.dto.MetroDetailResponseDto;
import com.metro.connect.dto.ScheduleMetroResponseDto;
import com.metro.connect.entity.Metro;
import com.metro.connect.entity.MetroSeat;
import com.metro.connect.entity.ScheduleMetro;
import com.metro.connect.exception.MetroBookingAddTicketsFailedException;
import com.metro.connect.external.MetroBookingService;
import com.metro.connect.external.MetroLocationService;
import com.metro.connect.service.MetroSeatService;
import com.metro.connect.service.MetroService;
import com.metro.connect.service.ScheduleMetroService;
import com.metro.connect.utility.DateTimeUtil;
import com.metro.connect.utility.MetroSeatGenerator;
import com.metro.connect.utility.ScheduleMetroReferenceNumberGenerator;



@Component
public class MetroResource {

	private static  final Logger logger =  LoggerFactory.getLogger(MetroResource.class);


	@Autowired
	private MetroService metroService;

	@Autowired
	private MetroSeatService metroSeatService;

	@Autowired
	private ScheduleMetroService scheduleMetroService;

	@Autowired
	private MetroLocationService metroLocationService;
	
	@Autowired
	private MetroBookingService metroBookingService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Transactional
	public ResponseEntity<CommonApiResponse> addMetro(Metro metro) {

		CommonApiResponse response = new CommonApiResponse();

		if (metro == null) {
			response.setResponseMessage("Bad Request, Metro details not proper");
			response.setSuccess(true);
			response.setStatus(HttpStatus.BAD_REQUEST);
			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Metro existingMetro = metroService.getMetroByNumber(metro.getNumber());

		if (existingMetro != null) {
			response.setResponseMessage("Metro already exist with this Number");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		metro.setStatus(MetroStatus.ACTIVE.value());

		// adding metro in database
		existingMetro = this.metroService.addMetro(metro);

		if (existingMetro == null) {
			response.setResponseMessage("failed to register metro");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		// adding metro seat in database

		List<MetroSeat> metroSeats = MetroSeatGenerator.generateMetroSeat(metro.getTotalCoach(),
				metro.getTotalSeatInEachCoach(), existingMetro.getId());

		List<MetroSeat> addedMetroSeats = metroSeatService.addMetroSeats(metroSeats);

		if (addedMetroSeats == null) {
			System.out.println("Failed to add metro seats");
		}

		response.setResponseMessage("Metro registered Successfully");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> updateMetro(Metro metro) {

		CommonApiResponse response = new CommonApiResponse();

		if (metro == null) {
			response.setResponseMessage("Bad Request, Metro details not proper");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Metro existingMetro = metroService.getMetroById(metro.getId());

		if (existingMetro == null) {
			response.setResponseMessage("Metro not found in database");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		// during update we can edit only 3 things
		existingMetro.setName(metro.getName());
		existingMetro.setNumber(metro.getNumber());
		existingMetro.setSeatPrice(metro.getSeatPrice());

		// adding metro in database
		Metro updatedMetro = this.metroService.updateMetro(existingMetro);

		if (updatedMetro == null) {
			response.setResponseMessage("failed to update metro");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Metro updated Successfully");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> deleteMetro(int metroId) {

		CommonApiResponse response = new CommonApiResponse();

		if (metroId == 0) {
			response.setResponseMessage("Bad request, Metro Id is 0");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Metro metro = this.metroService.getMetroById(metroId);

		if (metro == null) {
			response.setResponseMessage("Metro not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		metro.setStatus(MetroStatus.NOT_ACTIVE.value());

		Metro deletedMetro = this.metroService.updateMetro(metro);

		if (deletedMetro == null) {
			response.setResponseMessage("Failed to delete the Metro");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Metro deleted Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);
	}

	public ResponseEntity<MetroDetailResponseDto> fetchMetroDetail(int metroId) {

		MetroDetailResponseDto response = new MetroDetailResponseDto();

		if (metroId == 0) {
			response.setResponseMessage("Bad request, Metro Id is 0");
			response.setSuccess(true);

			return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Metro metro = this.metroService.getMetroById(metroId);

		if (metro == null) {
			response.setResponseMessage("Metro not found");
			response.setSuccess(true);

			return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		MetroDetail metroDetail = new MetroDetail();
		metroDetail.setId(metro.getId());
		metroDetail.setName(metro.getName());
		metroDetail.setNumber(metro.getNumber());
		metroDetail.setSeatPrice(metro.getSeatPrice());
		metroDetail.setTotalCoach(metro.getTotalCoach());
		metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());

		// hitting location service for fetching location using id
		LocationResponseDto fromLocation = this.metroLocationService.fetchLocationById(metro.getFromLocationId());
		LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

		metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
		metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

		response.setMetro(Arrays.asList(metroDetail));
		response.setResponseMessage("Metro Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<MetroDetailResponseDto> fetchAllMetros() {

		MetroDetailResponseDto response = new MetroDetailResponseDto();

		List<MetroDetail> metroDetails = new ArrayList<>();

		List<Metro> metros = this.metroService.getMetroByStatus(MetroStatus.ACTIVE.value());

		logger.info("List of Metros {}", metros.toString());

		if (metros == null) {
			response.setResponseMessage("No Active Metros found in database");
			response.setSuccess(true);

			return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.OK);
		}

		for (Metro metro : metros) {
			MetroDetail metroDetail = new MetroDetail();
			metroDetail.setId(metro.getId());
			metroDetail.setName(metro.getName());
			metroDetail.setNumber(metro.getNumber());
			metroDetail.setSeatPrice(metro.getSeatPrice());
			metroDetail.setTotalCoach(metro.getTotalCoach());
			metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());

			// hitting location service for fetching location using id
			LocationResponseDto fromLocation = this.metroLocationService.fetchLocationById(metro.getFromLocationId());
			LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

			metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
			metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

			metroDetails.add(metroDetail);
		}

		response.setMetro(metroDetails);
		response.setResponseMessage("Metro Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<MetroDetailResponseDto> fetchMetroDetailsFromAndToLocation(int fromLocationId,
			int toLocationId) {

		MetroDetailResponseDto response = new MetroDetailResponseDto();

		if (fromLocationId == 0 || toLocationId == 0) {
			response.setResponseMessage("Bad request, Location not selected");
			response.setSuccess(true);

			return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Metro> metros = this.metroService.getMetroByFromAndToLocation(fromLocationId, toLocationId,
				MetroStatus.ACTIVE.value());

		if (metros == null) {
			response.setResponseMessage("Metros not found");
			response.setSuccess(true);

			return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<MetroDetail> metroDetails = new ArrayList<>();

		// hitting location service for fetching location using id
		LocationResponseDto fromLocation = this.metroLocationService.fetchLocationById(fromLocationId);
		LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(toLocationId);

		for (Metro metro : metros) {
			MetroDetail metroDetail = new MetroDetail();
			metroDetail.setId(metro.getId());
			metroDetail.setName(metro.getName());
			metroDetail.setNumber(metro.getNumber());
			metroDetail.setSeatPrice(metro.getSeatPrice());
			metroDetail.setTotalCoach(metro.getTotalCoach());
			metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
			metroDetail.setToLocation(toLocation.getLocation().get(0).getName());
			metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());

			metroDetails.add(metroDetail);
		}

		response.setMetro(metroDetails);
		response.setResponseMessage("Metro Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<MetroDetailResponseDto> searchMetro(String metroNumber) {

		MetroDetailResponseDto response = new MetroDetailResponseDto();

		if (metroNumber == null) {
			response.setResponseMessage("Bad request, metroNumber found empty");
			response.setSuccess(true);

			return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Metro> metros = this.metroService.getAllMetrosByNumber(metroNumber, MetroStatus.ACTIVE.value());

		if (metros == null) {
			response.setResponseMessage("Metros not found");
			response.setSuccess(true);

			return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<MetroDetail> metroDetails = new ArrayList<>();

		for (Metro metro : metros) {
			MetroDetail metroDetail = new MetroDetail();
			metroDetail.setId(metro.getId());
			metroDetail.setName(metro.getName());
			metroDetail.setNumber(metro.getNumber());
			metroDetail.setSeatPrice(metro.getSeatPrice());
			metroDetail.setTotalCoach(metro.getTotalCoach());
			metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());

			// hitting location service for fetching location using id
			LocationResponseDto fromLocation = this.metroLocationService.fetchLocationById(metro.getFromLocationId());
			LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

			metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
			metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

			metroDetails.add(metroDetail);
		}

		response.setMetro(metroDetails);
		response.setResponseMessage("Metro Fetched Successfully");
		response.setSuccess(true);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<MetroDetailResponseDto>(response, HttpStatus.OK);
	}

	@Transactional
	public ResponseEntity<CommonApiResponse> scheduleMetro(ScheduleMetro scheduleMetro) {

		CommonApiResponse response = new CommonApiResponse();

		if (scheduleMetro == null) {
			response.setResponseMessage("Bad Request, Schedule data not proper");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (scheduleMetro.getMetroId() == 0) {
			response.setResponseMessage("Bad Request, Metro not selected");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}


		logger.info("Getting Metro by metro Id {}", scheduleMetro.getMetroId());

		Metro metro = this.metroService.getMetroById(scheduleMetro.getMetroId());

		if (metro == null || metro.getStatus() != MetroStatus.ACTIVE.value()) {
			response.setResponseMessage("Bad Request, Metro not Active");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (scheduleMetro.getScheduleTime() == null) {
			response.setResponseMessage("Bad Request, Schedule time not proper");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		scheduleMetro.setStatus(MetroStatus.ACTIVE.value());
		scheduleMetro.setScheduleMetroId(ScheduleMetroReferenceNumberGenerator.generate());

		ScheduleMetro scheduledMetro = this.scheduleMetroService.scheduleMetro(scheduleMetro);

		if (scheduledMetro != null) {
			
			// add metro seats in Booking during Metro schedule only
             
			List<MetroSeat> metroSeats = new ArrayList<>();
			metroSeats = this.metroSeatService.getMetroSeatByMetroId(metro.getId());
			
			AddScheduleMetroTicketRequestDto addScheduleMetroTicketRequestDto = new AddScheduleMetroTicketRequestDto();
			addScheduleMetroTicketRequestDto.setMetroSeats(metroSeats);
			addScheduleMetroTicketRequestDto.setScheduleMetroId(scheduledMetro.getId());
			
			ResponseEntity<CommonApiResponse> addTicketResponse = this.metroBookingService.addScheduledMetroTickets(addScheduleMetroTicketRequestDto);
			
			if(addTicketResponse == null) {
				throw new MetroBookingAddTicketsFailedException("Failed to add Scheduled Metro Tickets in Booking Table...!!!");
			} 
			
			response.setResponseMessage("Metro Successfully Scheduled..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);

		}

		else {
			response.setResponseMessage("Failed to schedule Metro..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleMetroResponseDto> getScheduledMetroById(int scheduleMetroId) {

		ScheduleMetroResponseDto response = new ScheduleMetroResponseDto();

		if (scheduleMetroId == 0) {
			response.setResponseMessage("Bad Request, Schedule Metro Id is missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		ScheduleMetro scheduleMetro = this.scheduleMetroService.getByScheduleMetroId(scheduleMetroId);

		if (scheduleMetro == null) {
			response.setResponseMessage("Bad Request, Schedule metro not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Metro metro = this.metroService.getMetroById(scheduleMetro.getMetroId());

		if (metro == null) {
			response.setResponseMessage("Bad Request, metro not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<MetroDetail> metroDetails = new ArrayList<>();

		List<MetroSeat> metroSeats = this.metroSeatService.getMetroSeatByMetroId(metro.getId());

		if (metroSeats == null) {
			response.setResponseMessage("Bad Request, metroSeats not found in database");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		MetroDetail metroDetail = new MetroDetail();
		metroDetail.setId(metro.getId());
		metroDetail.setName(metro.getName());
		metroDetail.setNumber(metro.getNumber());
		metroDetail.setSeatPrice(metro.getSeatPrice());
		metroDetail.setTotalCoach(metro.getTotalCoach());
		metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
		metroDetail.setScheduleTime(scheduleMetro.getScheduleTime());
		metroDetail.setScheduleMetroId(scheduleMetro.getScheduleMetroId());
		metroDetail.setMetroSeats(metroSeats);

		// hitting location service for fetching location using id
		LocationResponseDto fromLocation = this.metroLocationService.fetchLocationById(metro.getFromLocationId());
		LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

		metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
		metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

		metroDetails.add(metroDetail);

		response.setMetro(metroDetails);
		response.setResponseMessage("Scheduled Metro fetched success..!!!");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<CommonApiResponse> deleteScheduleMetro(String scheduleMetroId) {

		CommonApiResponse response = new CommonApiResponse();

		if (scheduleMetroId == null) {
			response.setResponseMessage("Bad Request, scheduleMetroId not found");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		ScheduleMetro scheduledMetro = this.scheduleMetroService.getByScheduleMetroId(scheduleMetroId);

		if (scheduledMetro == null || scheduledMetro.getStatus() != MetroStatus.ACTIVE.value()) {
			response.setResponseMessage("Bad Request, Scheduled Metro is already In-Active");
			response.setSuccess(true);

			return new ResponseEntity<CommonApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		scheduledMetro.setStatus(MetroStatus.NOT_ACTIVE.value());

		ScheduleMetro deletedMetro = this.scheduleMetroService.scheduleMetro(scheduledMetro);

		if (deletedMetro != null) {
			response.setResponseMessage("Deleted Scheduled Metro..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to delete schedule Metro..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<CommonApiResponse>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleMetroResponseDto> searchScheduledMetroByTime(String startTime, String endTime) {

		ScheduleMetroResponseDto response = new ScheduleMetroResponseDto();

		if (startTime == null || endTime == null) {
			response.setResponseMessage("Bad Request, start time or end time missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleMetro> scheduledMetros = new ArrayList<>();

		scheduledMetros = this.scheduleMetroService.getByScheduleTimeBetweenAndStatus(startTime, endTime,
				MetroStatus.ACTIVE.value());

		List<MetroDetail> metroDetails = new ArrayList<>();

		if (!scheduledMetros.isEmpty()) {

			for (ScheduleMetro scheduleMetro : scheduledMetros) {
				Metro metro = this.metroService.getMetroById(scheduleMetro.getMetroId());

				MetroDetail metroDetail = new MetroDetail();
				metroDetail.setId(metro.getId());
				metroDetail.setName(metro.getName());
				metroDetail.setNumber(metro.getNumber());
				metroDetail.setSeatPrice(metro.getSeatPrice());
				metroDetail.setTotalCoach(metro.getTotalCoach());
				metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
				metroDetail.setScheduleTime(scheduleMetro.getScheduleTime());
				metroDetail.setScheduleMetroId(scheduleMetro.getScheduleMetroId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.metroLocationService
						.fetchLocationById(metro.getFromLocationId());
				LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

				metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

				metroDetails.add(metroDetail);
			}

			response.setMetro(metroDetails);
			response.setResponseMessage("Scheduled Metro fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to schedule Metro..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleMetroResponseDto> fetchedScheduledMetro() {

		ScheduleMetroResponseDto response = new ScheduleMetroResponseDto();

		String startTime = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		if (startTime == null) {
			response.setResponseMessage("Bad Request, start time missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleMetro> scheduledMetros = new ArrayList<>();

		scheduledMetros = this.scheduleMetroService.getByScheduleTimeGreaterThanEqualAndStatus(startTime,
				MetroStatus.ACTIVE.value());

		List<MetroDetail> metroDetails = new ArrayList<>();

		if (!scheduledMetros.isEmpty()) {

			for (ScheduleMetro scheduleMetro : scheduledMetros) {
				Metro metro = this.metroService.getMetroById(scheduleMetro.getMetroId());

				MetroDetail metroDetail = new MetroDetail();
				metroDetail.setId(metro.getId());
				metroDetail.setName(metro.getName());
				metroDetail.setNumber(metro.getNumber());
				metroDetail.setSeatPrice(metro.getSeatPrice());
				metroDetail.setTotalCoach(metro.getTotalCoach());
				metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
				metroDetail.setScheduleTime(DateTimeUtil.getProperDateTimeFormatFromEpochTime(scheduleMetro.getScheduleTime()));
				metroDetail.setScheduleMetroId(scheduleMetro.getScheduleMetroId());
                metroDetail.setScheduleId(scheduleMetro.getId());
				
				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.metroLocationService
						.fetchLocationById(metro.getFromLocationId());
				LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

				metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

				metroDetails.add(metroDetail);
			}

		}

		response.setMetro(metroDetails);
		response.setResponseMessage("Scheduled Metros Fetched Successfully");
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleMetroResponseDto> fetchScheduledMetroByTime(String startTime, int metroId) {

		ScheduleMetroResponseDto response = new ScheduleMetroResponseDto();

		if (startTime == null || metroId == 0) {
			response.setResponseMessage("Bad Request, start time or metro id missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Metro metro = this.metroService.getMetroById(metroId);

		if (metro == null) {
			response.setResponseMessage("Bad Request, metro not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleMetro> scheduledMetros = new ArrayList<>();

		scheduledMetros = this.scheduleMetroService.getByScheduleTimeGreaterThanEqualAndStatusAndMetroId(startTime,
				MetroStatus.ACTIVE.value(), metroId);

		List<MetroDetail> metroDetails = new ArrayList<>();

		if (!scheduledMetros.isEmpty()) {

			for (ScheduleMetro scheduleMetro : scheduledMetros) {

				MetroDetail metroDetail = new MetroDetail();
				metroDetail.setId(metro.getId());
				metroDetail.setName(metro.getName());
				metroDetail.setNumber(metro.getNumber());
				metroDetail.setSeatPrice(metro.getSeatPrice());
				metroDetail.setTotalCoach(metro.getTotalCoach());
				metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
				metroDetail.setScheduleTime(scheduleMetro.getScheduleTime());
				metroDetail.setScheduleMetroId(scheduleMetro.getScheduleMetroId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.metroLocationService
						.fetchLocationById(metro.getFromLocationId());
				LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

				metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

				metroDetails.add(metroDetail);
			}

			response.setMetro(metroDetails);
			response.setResponseMessage("Scheduled Metro fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to schedule Metro..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleMetroResponseDto> fetchScheduledMetroByTimeRange(String startTime, String endTime,
			int metroId) {

		ScheduleMetroResponseDto response = new ScheduleMetroResponseDto();

		if (startTime == null || startTime == null || metroId == 0) {
			response.setResponseMessage("Bad Request, time or metro id missing");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		Metro metro = this.metroService.getMetroById(metroId);

		if (metro == null) {
			response.setResponseMessage("Bad Request, metro not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<ScheduleMetro> scheduledMetros = new ArrayList<>();

		scheduledMetros = this.scheduleMetroService.getByScheduleTimeBetweenAndStatusAndMetroId(startTime, endTime,
				MetroStatus.ACTIVE.value(), metroId);

		List<MetroDetail> metroDetails = new ArrayList<>();

		if (!scheduledMetros.isEmpty()) {

			for (ScheduleMetro scheduleMetro : scheduledMetros) {

				MetroDetail metroDetail = new MetroDetail();
				metroDetail.setId(metro.getId());
				metroDetail.setName(metro.getName());
				metroDetail.setNumber(metro.getNumber());
				metroDetail.setSeatPrice(metro.getSeatPrice());
				metroDetail.setTotalCoach(metro.getTotalCoach());
				metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
				metroDetail.setScheduleTime(scheduleMetro.getScheduleTime());
				metroDetail.setScheduleMetroId(scheduleMetro.getScheduleMetroId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.metroLocationService
						.fetchLocationById(metro.getFromLocationId());
				LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

				metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

				metroDetails.add(metroDetail);
			}

			response.setMetro(metroDetails);
			response.setResponseMessage("Scheduled Metro fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("Failed to schedule Metro..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.OK);

	}

	public ResponseEntity<ScheduleMetroResponseDto> fetchScheduledMetrosByLocationsAndCurrentTime(int sourceLocationId,
			int destinationLocationId) {

		ScheduleMetroResponseDto response = new ScheduleMetroResponseDto();

		List<Integer> metroIds = new ArrayList<>();

		if (sourceLocationId == 0 || destinationLocationId == 0) {
			response.setResponseMessage("Bad request, Location not selected");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		List<Metro> metros = this.metroService.getMetroByFromAndToLocation(sourceLocationId, destinationLocationId,
				MetroStatus.ACTIVE.value());

		if (metros == null) {
			response.setResponseMessage("Metros not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		for (Metro metro : metros) {
			metroIds.add(metro.getId());
		}

		String currentTimeInEpoch = String
				.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

		List<ScheduleMetro> scheduledMetros = this.scheduleMetroService
				.getByScheduleTimeGreaterThanEqualAndMetroIdInAndStatus(currentTimeInEpoch, metroIds,
						MetroStatus.ACTIVE.value());

		List<MetroDetail> metroDetails = new ArrayList<>();

		if (!scheduledMetros.isEmpty()) {

			for (ScheduleMetro scheduleMetro : scheduledMetros) {
				Metro metro = this.metroService.getMetroById(scheduleMetro.getMetroId());

				MetroDetail metroDetail = new MetroDetail();
				metroDetail.setId(metro.getId());
				metroDetail.setName(metro.getName());
				metroDetail.setNumber(metro.getNumber());
				metroDetail.setSeatPrice(metro.getSeatPrice());
				metroDetail.setTotalCoach(metro.getTotalCoach());
				metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
				metroDetail.setScheduleTime(
						DateTimeUtil.getProperDateTimeFormatFromEpochTime(scheduleMetro.getScheduleTime()));
				metroDetail.setScheduleMetroId(scheduleMetro.getScheduleMetroId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.metroLocationService
						.fetchLocationById(metro.getFromLocationId());
				LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

				metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

				metroDetails.add(metroDetail);
			}

			response.setMetro(metroDetails);
			response.setResponseMessage("Scheduled Metro fetched success..!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.OK);
		}

		else {
			response.setResponseMessage("No Scheduled metro found");
			response.setSuccess(true);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.OK);
	}

	public ResponseEntity<ScheduleMetroResponseDto> searchScheduledMetro(String startDate, String endDate,
			int sourceLocationId, int destinationLocationId) {
		
		ScheduleMetroResponseDto response = new ScheduleMetroResponseDto();
		
		if (startDate == null || endDate == null || sourceLocationId == 0 || destinationLocationId == 0) {
			response.setResponseMessage("Missing search input!!!");
			response.setSuccess(true);
			response.setStatus(HttpStatus.BAD_REQUEST);
		}

		List<Integer> metroIds = new ArrayList<>();

		List<Metro> metros = this.metroService.getMetroByFromAndToLocation(sourceLocationId, destinationLocationId,
				MetroStatus.ACTIVE.value());

		if (metros == null) {
			response.setResponseMessage("Metros not found");
			response.setSuccess(true);

			return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.BAD_REQUEST);
		}

		for (Metro metro : metros) {
			metroIds.add(metro.getId());
		}

		List<ScheduleMetro> scheduledMetros = this.scheduleMetroService
				.getByScheduleTimeBetweenAndStatusAndMetroIdIn(startDate, endDate, MetroStatus.ACTIVE.value(), metroIds);

		List<MetroDetail> metroDetails = new ArrayList<>();

		if (!scheduledMetros.isEmpty()) {

			for (ScheduleMetro scheduleMetro : scheduledMetros) {
				Metro metro = this.metroService.getMetroById(scheduleMetro.getMetroId());

				MetroDetail metroDetail = new MetroDetail();
				metroDetail.setId(metro.getId());
				metroDetail.setName(metro.getName());
				metroDetail.setNumber(metro.getNumber());
				metroDetail.setSeatPrice(metro.getSeatPrice());
				metroDetail.setTotalCoach(metro.getTotalCoach());
				metroDetail.setTotalSeatInEachCoach(metro.getTotalSeatInEachCoach());
				metroDetail.setScheduleTime(
						DateTimeUtil.getProperDateTimeFormatFromEpochTime(scheduleMetro.getScheduleTime()));
				metroDetail.setScheduleMetroId(scheduleMetro.getScheduleMetroId());

				// hitting location service for fetching location using id
				LocationResponseDto fromLocation = this.metroLocationService
						.fetchLocationById(metro.getFromLocationId());
				LocationResponseDto toLocation = this.metroLocationService.fetchLocationById(metro.getToLocationId());

				metroDetail.setFromLocation(fromLocation.getLocation().get(0).getName());
				metroDetail.setToLocation(toLocation.getLocation().get(0).getName());

				metroDetails.add(metroDetail);
			}

			response.setResponseMessage("Scheduled Metro fetched success..!!!");
		}

		else {
			response.setResponseMessage("No Scheduled metro found");
		}
		
		response.setMetro(metroDetails);
		response.setSuccess(true);
		response.setStatus(HttpStatus.OK);

		// Convert the object to a JSON string
		String jsonString = null;
		try {
			jsonString = objectMapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(jsonString);

		return new ResponseEntity<ScheduleMetroResponseDto>(response, HttpStatus.OK);
	
	}

}
