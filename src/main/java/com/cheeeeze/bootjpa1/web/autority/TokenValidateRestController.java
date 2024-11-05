package com.cheeeeze.bootjpa1.web.autority;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping( "/api" )
public class TokenValidateRestController {
	
	private final JwtTokenProvider jwtTokenProvider;
	
	/**
	 * @Description : JWT 토큰 유효성 검사
	 * @Date        : 2024. 10. 31.
	**/
	@GetMapping( "/validate-token" )
	public ResponseEntity<?> validateToken( @RequestHeader( "Authorization") String tokenHeader,
											@RequestParam( "userId" ) String userId ) {
		String token = tokenHeader.replace( "Bearer ", "" );
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.FORBIDDEN.value() );
		
		try {
			boolean isValid = jwtTokenProvider.validateToken( token, userId );
			map.put( "isValid", isValid );
			map.put( "status", HttpStatus.OK.value() );
		}
		catch ( Exception e ) {
			log.error( e.getMessage() );
			map.put( "error", e.getMessage() );
			return ResponseEntity.ok( map );
		}
		
		return ResponseEntity.ok( map );
	}
}
