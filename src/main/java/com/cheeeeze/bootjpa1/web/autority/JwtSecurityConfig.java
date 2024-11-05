package com.cheeeeze.bootjpa1.web.autority;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Description :
 * SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> 구현체.
 * 직접 만든 TokenProvider 와 JwtFilter 를 SecurityConfig 에 적용할 때 사용한다.
 * @Date        : 2024. 11. 5.
**/
@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public void configure( HttpSecurity httpSecurity ) throws Exception {
		JwtFilter jwtFilter = new JwtFilter(jwtTokenProvider);
		httpSecurity.addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class );
	}
}
