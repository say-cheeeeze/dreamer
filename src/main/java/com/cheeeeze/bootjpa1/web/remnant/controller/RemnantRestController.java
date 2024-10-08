package com.cheeeeze.bootjpa1.web.remnant.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/remnant/")
public class RemnantRestController {
	
	@PostMapping( "test")
	public ResponseEntity<?> test() {
		
		System.out.println( "Rest Controller Test" );
		List<String> strings = new ArrayList<>();
		strings.add( "test1" );
		strings.add( "test2" );
		strings.add( "test3" );
		
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.OK.value() );
		map.put( "list", strings );
		return ResponseEntity.ok( map );
	}
	
	@PostMapping( "regist" )
	public ResponseEntity<?> regist( @RequestBody RemnantInfo remnantInfo ) {
		
		System.out.println( "new remnant regist" + remnantInfo );
		
		Map<String, Object> map = new HashMap<>();
		
		map.put( "status", HttpStatus.OK.value() );
		map.put( "remnant", remnantInfo );
		return ResponseEntity.ok( map );
	}
}
