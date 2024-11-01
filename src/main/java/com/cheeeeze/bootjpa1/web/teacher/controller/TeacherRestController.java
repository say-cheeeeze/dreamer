package com.cheeeeze.bootjpa1.web.teacher.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherCnd;
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
	
	@PostMapping( "login" )
	public ResponseEntity<?> login( @RequestBody TeacherDTO teacherDTO ) {
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.NOT_FOUND.value() );
		
		try {
			// 아이디와 패스워드 두개 모두 일치하는 회원을 찾는다.
			Optional<TeacherInfo> byLoginIdAndPassword = teacherService.findByLoginIdAndPassword( teacherDTO.getLoginId(), teacherDTO.getPassword() );
			if ( byLoginIdAndPassword.isPresent() ) {
			
				String userToken = jwtUtil.generateToken( teacherDTO.getLoginId() );
				map.put( "status", HttpStatus.OK.value() );
				map.put( "teacherDto", byLoginIdAndPassword.get() );
				map.put( "token", userToken );
			}
			
		}
		catch( Exception e ) {
			log.error( "로그인 실패 {} ", e.getMessage() );
			return ResponseEntity.internalServerError().body( map );
		}
		return ResponseEntity.ok( map );
	}
	
	/**
	 * @Description : 교사 목록을 호출합니다.
	 * @Date        : 2024. 11. 1.
	**/
	@PostMapping( "/list" )
	public ResponseEntity<?> list( @RequestBody TeacherCnd cnd ) {
		
		Map<String, Object> map = new HashMap<>();
		
		Map<String, Object> pageInfo = teacherService.getTeacherPageListByCnd( cnd );
		
		map.put( "listInfo", pageInfo );
		map.put( "status", HttpStatus.OK.value() );
		return ResponseEntity.ok( map );
	}
}
