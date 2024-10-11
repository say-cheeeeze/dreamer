package com.cheeeeze.bootjpa1.web.remnant.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.remnant.service.RemnantService;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/remnant/")
@AllArgsConstructor
public class RemnantRestController {
	
	private RemnantService remnantService;
	
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
	
	/**
	 * @methodName   : list
	 * @author       : 남윤재
	 * @date         : 2024-10-10
	 * @description  : 렘넌트 페이징 정보 객체를 가져옵니다.
	 */
	@PostMapping( "/list" )
	public ResponseEntity<?> list( @RequestBody RemnantCnd cnd ) {
		
		Map<String, Object> map = new HashMap<>();
		
		Map<String, Object> pageInfo = remnantService.getRemnantPageListByCnd( cnd );
		
		map.put( "listInfo", pageInfo );
		map.put( "status", HttpStatus.OK.value() );
		return ResponseEntity.ok( map );
	}
}
