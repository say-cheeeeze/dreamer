package com.cheeeeze.bootjpa1.web.remnant.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.base.vo.SearchCnd;
import com.cheeeeze.bootjpa1.web.base.vo.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RemnantCnd extends SearchCnd {
	
	private Long id;
	private String name;
	private String grade;
	private Gender gender;
	private LocalDateTime inputDate;
	
	public RemnantCnd() {}
	
}
