package com.cheeeeze.bootjpa1.web.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
	
	MALE( "남" ),FEMALE("여");
	
	private final String gender;
	
	Gender( String gender ) {
		this.gender = gender;
	}
	
	@JsonValue
	public String getGender() {
		return gender;
	}
	
}

