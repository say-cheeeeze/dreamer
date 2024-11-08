package com.cheeeeze.bootjpa1.web.userauth.controller;

import java.util.HashMap;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.autority.JwtTokenProvider;
import com.cheeeeze.bootjpa1.web.base.util.DuplicationException;
import com.cheeeeze.bootjpa1.web.userauth.domain.UserRequestDTO;
import com.cheeeeze.bootjpa1.web.userauth.domain.UserResponseDTO;
import com.cheeeeze.bootjpa1.web.userauth.service.AuthService;
import com.cheeeeze.bootjpa1.web.autority.JwtTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping( "/auth" )
@RequiredArgsConstructor
public class AuthRestController {
	
	private final AuthService authService;
	private final JwtTokenProvider jwtTokenProvider;
	
	@PostMapping( "/signup" )
	public ResponseEntity<?> signup( @RequestBody UserRequestDTO userRequestDTO ) {
		
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.OK.value() );
		
		try {
			UserResponseDTO user = authService.signup( userRequestDTO );
			JwtTokenInfo token = authService.setTokenByUserRequest( userRequestDTO );
			map.put( "user", user );
			map.put( "token", token );
		}
		catch( DuplicationException e ) {
			log.info( e.getMessage() );
			map.put( "status", e.getHttpStatus().value() ); // 409
			map.put( "msg", e.getMessage() );
		}
		
		return ResponseEntity.ok( map );
	}
	
	@PostMapping( "/login" )
	public ResponseEntity<JwtTokenInfo> login( @RequestBody UserRequestDTO requestDto ) {
		return ResponseEntity.ok( authService.setTokenByUserRequest( requestDto ) );
	}
	
	
	/**
	 * @Description : JWT 토큰 유효성 검사
	 * @Date : 2024. 10. 31.
	 **/
	@PostMapping( "/validate-token" )
	public ResponseEntity<?> validateToken( @RequestHeader( "Authorization" ) String tokenHeader,
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
