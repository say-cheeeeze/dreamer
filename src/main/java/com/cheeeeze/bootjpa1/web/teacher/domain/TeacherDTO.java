package com.cheeeeze.bootjpa1.web.teacher.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TeacherDTO {

	private Long id;
	private String loginId;
	private String name;
	private String password;
	private String email;
	private String phone;
	private LocalDateTime inputDate;
	private LocalDateTime updateDate;
	
	public TeacherInfo toTeacherInfo() {
		TeacherInfo.TeacherInfoBuilder builder = TeacherInfo.builder();
		builder.id( this.id );
		builder.name( this.name );
		builder.loginId(this.loginId);
		builder.password( this.password );
		builder.email( this.email );
		builder.phone( this.phone );
		builder.inputDate( this.inputDate );
		builder.updateDate( this.updateDate );
		return builder.build();
	}
}