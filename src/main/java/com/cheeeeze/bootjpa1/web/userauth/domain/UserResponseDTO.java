package com.cheeeeze.bootjpa1.web.userauth.domain;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherDTO;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
	
	private String loginId;
	private String name;
	
	public static UserResponseDTO fromInfo( TeacherInfo teacherInfo ) {
		return UserResponseDTO.builder()
							  .loginId( teacherInfo.getLoginId() )
							  .name( teacherInfo.getName() )
							  .build();
	}
}
