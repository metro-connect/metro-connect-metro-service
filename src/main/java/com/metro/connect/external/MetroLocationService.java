package com.metro.connect.external;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.metro.connect.dto.LocationResponseDto;

@Component
@FeignClient(name = "metro-connect-location-service")
public interface MetroLocationService {
	
	@GetMapping("api/location/fetch")
	LocationResponseDto fetchLocationById(@RequestParam("locationId") int  locationId);
	
}
