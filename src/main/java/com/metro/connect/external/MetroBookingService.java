package com.metro.connect.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.metro.connect.dto.AddScheduleMetroTicketRequestDto;
import com.metro.connect.dto.CommonApiResponse;

@Component
@FeignClient(name = "ticket-booking-book-service")
public interface MetroBookingService {
	
	@PostMapping("/api/book/add/scheduled/metro/tickets/")
	public ResponseEntity<CommonApiResponse> addScheduledMetroTickets(@RequestBody AddScheduleMetroTicketRequestDto ticketAddRequest);

}
