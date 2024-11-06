package com.cheeeeze.bootjpa1.web.userauth.service;

import java.util.Collections;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.base.Authority;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername( String loginId ) throws UsernameNotFoundException {
		
		Optional<TeacherInfo> byLoginId = userRepository.findByLoginId( loginId );
		if ( byLoginId.isEmpty() ) {
			throw new RuntimeException( loginId + "가 존재하지 않습니다" );
		}
		
		return createUserDetails( byLoginId.get() );
	}
	
	private UserDetails createUserDetails( TeacherInfo teacherInfo ) {
		
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority( teacherInfo.getAuthority()
																				   .toString() );
		
		return new User( teacherInfo.getLoginId(),
						 teacherInfo.getPassword(),
						 Collections.singleton( grantedAuthority )
		);
	}
}
