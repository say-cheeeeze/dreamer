package com.cheeeeze.bootjpa1.web.teacher.controller;

import java.util.HashMap;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherDTO;
import com.cheeeeze.bootjpa1.web.teacher.service.TeacherService;
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
	
	@PostMapping( "save" )
	public ResponseEntity<?> saveTeacher( @RequestBody TeacherDTO teacherDTO ) {
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.OK.value() );
		
		log.info( teacherDTO.toString() );
		return ResponseEntity.ok( map );
	}
}
