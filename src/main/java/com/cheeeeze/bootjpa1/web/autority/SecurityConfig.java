package com.cheeeeze.bootjpa1.web.autority;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@Component
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDenyHandler jwtAccessDenyHandler;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
		
		http.csrf( AbstractHttpConfigurer::disable )
			.authorizeHttpRequests( authReq -> authReq.requestMatchers( "/api/**" ).hasAnyRole( "TEACHER", "ADMIN" )
													  .requestMatchers( "/auth/**" ).permitAll()
													  .anyRequest().authenticated()
			)
			.exceptionHandling(
				exceptionHandling -> exceptionHandling.authenticationEntryPoint( jwtAuthenticationEntryPoint )
													  .accessDeniedHandler( jwtAccessDenyHandler )
			)
			.apply( new JwtSecurityConfig( jwtTokenProvider ) );
		
		return http.build();
	}
}
