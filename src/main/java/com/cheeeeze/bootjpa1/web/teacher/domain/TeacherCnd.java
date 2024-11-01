package com.cheeeeze.bootjpa1.web.teacher.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.base.SearchCnd;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeacherCnd extends SearchCnd {
	
	private Long id;
	private String name;
	private String email;
	private String phone;
	private LocalDateTime inputDate;
	
	public TeacherCnd() {}
	
}
