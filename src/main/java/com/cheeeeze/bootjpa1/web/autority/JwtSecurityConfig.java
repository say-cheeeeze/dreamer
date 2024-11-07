package com.cheeeeze.bootjpa1.web.autority;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurer;
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
public class JwtSecurityConfig implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public void init( HttpSecurity httpSecurity ) throws Exception {
	}
	
	@Override
	public void configure( HttpSecurity httpSecurity ) throws Exception {
		JwtFilter jwtFilter = new JwtFilter( jwtTokenProvider );
		
		// jwtFilter 를 UsernamePasswordAuthenticationFilter 앞에 두겠다.
		
		// Spring security 는
		// UsernamePasswordAuthenticationFilter 를 처리하기전에 jwtFilter 를 실행한다.
		
		// UsernamePasswordAuthenticationFilter 는 폼 전송이나 기본인증방식을 처리하는 필터인데
		// JWT 토큰을 먼저 검사하겠다는 얘기
		httpSecurity.addFilterBefore( jwtFilter, UsernamePasswordAuthenticationFilter.class );
	}
}
