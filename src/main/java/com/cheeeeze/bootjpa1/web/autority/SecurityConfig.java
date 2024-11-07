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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
	
	// CORS 설정
//	@Bean
//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration configuration = new CorsConfiguration();
//		configuration.addAllowedOrigin( "http://localhost:3000" ); // 프론트엔드 URL 추가
//		configuration.addAllowedMethod( "*" ); // 모든 HTTP 메서드 허용
//		configuration.addAllowedHeader( "*" ); // 모든 헤더 허용
//		configuration.setAllowCredentials( true ); // 쿠키, 인증 정보 허용
//
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration( "/**", configuration );
//		return source;
//	}
	@Bean
	public SecurityFilterChain securityFilterChain( HttpSecurity http ) throws Exception {
		
		http
//			.cors( cors -> cors.configurationSource( corsConfigurationSource() ))
			.csrf( AbstractHttpConfigurer::disable )
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
