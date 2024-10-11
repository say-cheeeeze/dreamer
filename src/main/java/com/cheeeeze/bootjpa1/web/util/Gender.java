package com.cheeeeze.bootjpa1.web.util;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
	
	MALE( "남자" ),FEMALE("여자");
	
	private final String gender;
	
	Gender(String gender) {
		this.gender = gender;
	}
	
	@JsonValue
	public String getGender() {
		return gender;
	}
}
