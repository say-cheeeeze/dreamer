package com.cheeeeze.bootjpa1.web.autority;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtAccessDenyHandler implements AccessDeniedHandler {
	
	// 필요한 권한이 없이 접근하려 할 때 호출된다.
	@Override
	public void handle( HttpServletRequest request,
						HttpServletResponse response,
						AccessDeniedException accessDeniedException ) throws IOException, ServletException {
		response.sendError( HttpServletResponse.SC_FORBIDDEN );
	}
}