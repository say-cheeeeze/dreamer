package com.cheeeeze.bootjpa1.web.autority;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;
	
	private String resolveToken( HttpServletRequest request ) {
		String bearerToken = request.getHeader( AUTHORIZATION_HEADER );
		
		if ( null == bearerToken || !bearerToken.startsWith( BEARER_PREFIX ) ) {
			return null;
		}
		return bearerToken.substring( BEARER_PREFIX.length() );
	}
	
	@Override
	protected void doFilterInternal( HttpServletRequest request,
									 HttpServletResponse response,
									 FilterChain filterChain ) throws ServletException, IOException {
	
		String jwt = resolveToken( request );
		
		if ( !StringUtils.hasText( jwt ) || !jwtTokenProvider.validateToken( jwt ) ) {
			return;
		}
		Authentication auth = jwtTokenProvider.getAuthentication( jwt );
		SecurityContextHolder.getContext().setAuthentication( auth );
		
		filterChain.doFilter( request, response );
	}
}
