package com.metro.connect.dto;

import java.util.List;

public class MetroDetailResponseDto extends CommonApiResponse {

	private List<MetroDetail> metro;

	public List<MetroDetail> getMetro() {
		return metro;
	}

	public void setMetro(List<MetroDetail> metro) {
		this.metro = metro;
	}

}
