package com.cheeeeze.bootjpa1.web.autority;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
	
	private SecurityUtil() {
	}
	
	public static String getCurrentUserId() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if ( null == authentication || null == authentication.getName() ) {
			throw new RuntimeException( "인증정보가 존재하지 않습니다" );
		}
		return authentication.getName();
	}
}
