package com.cheeeeze.bootjpa1.web.autority;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
	public SecurityFilterChain securityFilterChain( HttpSecurity http) throws Exception {
		http.authorizeHttpRequests( ( authorize ) -> authorize.anyRequest().authenticated() )
			.httpBasic( Customizer.withDefaults() )
			.formLogin( Customizer.withDefaults() );
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
									  .username( "TEST" )
									  .password( "password" ).build();
		return new InMemoryUserDetailsManager( userDetails );
	}
}
