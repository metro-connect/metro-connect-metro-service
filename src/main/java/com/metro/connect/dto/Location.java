package com.metro.connect.dto;

import org.springframework.lang.NonNull;

public class Location {
	
	private int id;
	
	@NonNull
    private String name;
    
	@NonNull
    private String description;
	
    private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
    	
}
