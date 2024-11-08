package com.cheeeeze.bootjpa1.web.userauth.domain;

import com.cheeeeze.bootjpa1.web.base.vo.Authority;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDTO {
	
	private String loginId;
	private String password;
	private String name;
	private String email;
	private String phone;
	
	public TeacherInfo toTeacher( PasswordEncoder passwordEncoder ) {
		return TeacherInfo.builder()
						  .loginId( loginId )
						  .name( name )
						  .email( email )
						  .phone( phone )
						  .password( passwordEncoder.encode( password ) )
						  .authority( Authority.ROLE_TEACHER )
						  .build();
	}
	
	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken( loginId, password );
	}
	
}
