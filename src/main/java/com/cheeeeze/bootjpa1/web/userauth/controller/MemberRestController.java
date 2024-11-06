package com.cheeeeze.bootjpa1.web.userauth.controller;

import com.cheeeeze.bootjpa1.web.userauth.domain.UserResponseDTO;
import com.cheeeeze.bootjpa1.web.userauth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/member" )
@RequiredArgsConstructor
@Slf4j
public class MemberRestController {
	
	private final UserService userService;
	
	@PostMapping( "/me" )
	public ResponseEntity<UserResponseDTO> getMyMemberInfo() {
		UserResponseDTO myInfoBySecurity = userService.getUserInfoBySecurity();
		
		log.info( myInfoBySecurity.getLoginId() );
		
		return ResponseEntity.ok( myInfoBySecurity );
	}
}
