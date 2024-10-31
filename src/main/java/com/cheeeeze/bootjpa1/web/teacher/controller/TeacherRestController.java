package com.cheeeeze.bootjpa1.web.teacher.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherDTO;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import com.cheeeeze.bootjpa1.web.teacher.service.TeacherService;
import com.cheeeeze.bootjpa1.web.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping( "/api/teacher/")
public class TeacherRestController {

	private final TeacherService teacherService;
	private final JWTUtil jwtUtil;
	
	@PostMapping( "save" )
	public ResponseEntity<?> saveTeacher( @RequestBody TeacherDTO teacherDTO ) {
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.INTERNAL_SERVER_ERROR.value() );
		
		try {
			
			Optional<TeacherInfo> byLoginId = teacherService.findByLoginId( teacherDTO.getLoginId() );
			if ( byLoginId.isPresent() ) {
				map.put( "status", HttpStatus.CONFLICT.value() );
				map.put( "message", "존재하는 아이디입니다" );
				return ResponseEntity.ok( map );
			}
			
			TeacherDTO teacherDto = teacherService.insertTeacher( teacherDTO );
			
			String userToken = jwtUtil.generateToken( teacherDTO.getLoginId() );
			
			map.put( "status", HttpStatus.OK.value() );
			map.put( "teacherDto", teacherDto );
			map.put( "token", userToken );
		}
		catch( Exception e ) {
			log.error( "데이터 저장 실패 {} ", e.getMessage() );
			return ResponseEntity.internalServerError().body( map );
		}
		
		return ResponseEntity.ok( map );
	}
}
