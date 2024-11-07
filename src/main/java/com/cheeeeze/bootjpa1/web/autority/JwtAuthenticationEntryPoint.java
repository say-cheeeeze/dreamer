package com.cheeeeze.bootjpa1.web.autority;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	// commence : 개시하다
	@Override
	public void commence( HttpServletRequest request,
						  HttpServletResponse response,
						  AuthenticationException authException ) throws IOException {
	
		log.info( "유효하지 않은 자격증명입니다." );
		response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
	}
}
