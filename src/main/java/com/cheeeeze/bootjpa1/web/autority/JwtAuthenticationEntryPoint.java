package com.cheeeeze.bootjpa1.web.autority;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
		
		response.setContentType( "application/json" );
		response.setCharacterEncoding( "UTF-8" );
		response.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
		
		Map<String, Object> responseData = new HashMap<>();
		String msg = "유효하지 않은 자격 증명입니다";
		
		if ( authException instanceof InternalAuthenticationServiceException ||
			 authException instanceof UsernameNotFoundException ) {
			msg = "일치하는 회원이 없습니다";
		}
		else if ( authException instanceof BadCredentialsException ) {
			msg = "비밀번호가 일치하지 않습니다";
		}
		
		log.info( msg );
		responseData.put( "msg", msg );
		response.getWriter().write( new Gson().toJson( responseData ) );
	}
}
