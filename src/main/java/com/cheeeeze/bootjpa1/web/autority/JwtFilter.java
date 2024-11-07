package com.cheeeeze.bootjpa1.web.autority;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private final JwtTokenProvider jwtTokenProvider;
	
	/**
	 * @desc httpServlet요청객체에서 토큰값만 조회합니다.
	 * @author cheeeeze
	 * @param request
	 * @return 헤더토큰값
	 * @date   2024. 11. 7.
	 */
	private String parseTokenFromHeader( HttpServletRequest request ) {
		String bearerToken = request.getHeader( AUTHORIZATION_HEADER );
		
		if ( null == bearerToken || !bearerToken.startsWith( BEARER_PREFIX ) ) {
			return null;
		}
		return bearerToken.substring( BEARER_PREFIX.length() );
	}
	
	/**
	 * UsernamePasswordAuthenticationFilter 보다 먼저 JwtFilter 가 수행된다.
	 * 따라서 이 메소드가 먼저 실행된다.
	 *
	 * doFilter 는 애플리케이션의 필터가 처리할
	 * 다음 필터 또는 서블릿으로 요청을 넘기는 역할을 한다.
	 *
	 * @desc
	 * @author cheeeeze
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 * @date   2024. 11. 7.
	 */
	@Override
	protected void doFilterInternal( HttpServletRequest request,
									 HttpServletResponse response,
									 FilterChain filterChain ) throws ServletException, IOException {
	
		String jwt = parseTokenFromHeader( request );
		
		String requestURI = request.getRequestURI();
		
		log.info( "REQUEST URI : {}", requestURI );
		
		if ( requestURI.startsWith( "/auth" ) || requestURI.startsWith( "/api" ) ) {
			filterChain.doFilter( request, response );
			return;
		}
		
		// 토큰이 없거나 유효하지 않은 경우
		if ( !StringUtils.hasText( jwt ) || !jwtTokenProvider.validateToken( jwt ) ) {
			log.info( "토큰이 없거나 유효하지 않습니다." );
			response.sendError( HttpServletResponse.SC_UNAUTHORIZED );
			return;
		}
		Authentication auth = jwtTokenProvider.getAuthentication( jwt );
		SecurityContextHolder.getContext().setAuthentication( auth );
		
		filterChain.doFilter( request, response );
	}
}
