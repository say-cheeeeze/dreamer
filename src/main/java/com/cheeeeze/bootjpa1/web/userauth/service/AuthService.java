package com.cheeeeze.bootjpa1.web.userauth.service;

import com.cheeeeze.bootjpa1.web.autority.JwtTokenInfo;
import com.cheeeeze.bootjpa1.web.base.util.DuplicationException;
import com.cheeeeze.bootjpa1.web.userauth.domain.UserRequestDTO;
import com.cheeeeze.bootjpa1.web.userauth.domain.UserResponseDTO;
import com.cheeeeze.bootjpa1.web.autority.JwtTokenProvider;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
	private final AuthenticationManagerBuilder authMgrBuilder;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	public UserResponseDTO signup( UserRequestDTO userRequestDTO ) {
		
		if ( userRepository.existsByLoginId( userRequestDTO.getLoginId() ) ) {
			throw new DuplicationException( "이미 가입된 아이디입니다" );
		}
		TeacherInfo teacherInfo = userRequestDTO.toTeacher( passwordEncoder );
		return UserResponseDTO.fromInfo( userRepository.save( teacherInfo ) );
	}
	
	public JwtTokenInfo setTokenByUserRequest( UserRequestDTO userRequestDTO ) {
		
		UsernamePasswordAuthenticationToken authenticationToken = userRequestDTO.toAuthentication();
		
		AuthenticationManager authManager = authMgrBuilder.getObject();
		Authentication authenticate = authManager.authenticate( authenticationToken );
		
		return jwtTokenProvider.generateToken( authenticate );
	}
}
