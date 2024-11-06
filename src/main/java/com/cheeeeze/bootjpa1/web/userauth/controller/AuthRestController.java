package com.cheeeeze.bootjpa1.web.userauth.controller;

import java.util.HashMap;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.userauth.domain.UserRequestDTO;
import com.cheeeeze.bootjpa1.web.userauth.domain.UserResponseDTO;
import com.cheeeeze.bootjpa1.web.userauth.service.AuthService;
import com.cheeeeze.bootjpa1.web.autority.JwtTokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping( "/auth" )
@RequiredArgsConstructor
public class AuthRestController {
	
	private final AuthService authService;
	
	@PostMapping( "/signup" )
	public ResponseEntity<UserResponseDTO> signup( @RequestBody UserRequestDTO userRequestDTO ) {
		return ResponseEntity.ok( authService.signup( userRequestDTO ) );
	}
	
	@PostMapping( "/login" )
	public ResponseEntity<JwtTokenInfo> login( @RequestBody UserRequestDTO requestDto ) {
		return ResponseEntity.ok( authService.login( requestDto ) );
	}
	
}
