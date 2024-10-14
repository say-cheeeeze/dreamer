package com.cheeeeze.bootjpa1.web.remnant.controller;

import java.util.HashMap;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.remnant.service.RemnantService;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping( "/api/remnant" )
@AllArgsConstructor
public class RemnantRestController {
	
	private RemnantService remnantService;
	
	/**
	 * @description  : 단건 객체를 신규 등록 또는 수정합니다.
	 * @author       : 남윤재
	 * @date         : 2024-10-13
	 */
	@PostMapping( "/save" )
	public ResponseEntity<?> save( @RequestBody RemnantInfo remnantInfo ) {
		
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.INTERNAL_SERVER_ERROR.value() );
		
		try {
			RemnantInfo saveInfo = remnantService.saveRemnant( remnantInfo );
			map.put( "saveInfo", saveInfo );
			map.put( "status", HttpStatus.OK.value() );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body( map );
		}
		
		return ResponseEntity.ok( map );
	}
	
	/**
	 * @description  : 렘넌트 페이징 정보 객체를 가져옵니다.
	 * @author       : 남윤재
	 * @date         : 2024-10-10
	 */
	@PostMapping( "/list" )
	public ResponseEntity<?> list( @RequestBody RemnantCnd cnd ) {
		
		Map<String, Object> map = new HashMap<>();
		
		Map<String, Object> pageInfo = remnantService.getRemnantPageListByCnd( cnd );
		
		map.put( "listInfo", pageInfo );
		map.put( "status", HttpStatus.OK.value() );
		return ResponseEntity.ok( map );
	}
	
	/**
	 * @description  : 렘넌트 단건 정보를 가져옵니다.
	 * @author       : 남윤재
	 * @date         : 2024-10-12
	 */
	@PostMapping( "/get" )
	public ResponseEntity<?> get( @RequestBody RemnantCnd cnd ) {
		
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.INTERNAL_SERVER_ERROR.value() );
		
		try {
			RemnantInfo getInfo = remnantService.getRemnantInfo( cnd );
			map.put( "info", getInfo );
			map.put( "status", HttpStatus.OK.value() );
		}
		catch ( Exception e ) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body( map );
		}
		
		return ResponseEntity.ok( map );
	}
	
	@RequestMapping( "/delete" )
	public ResponseEntity<?> delete( @RequestBody RemnantCnd cnd ) {
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.INTERNAL_SERVER_ERROR.value() );
		try {
			remnantService.deleteRemnantById( cnd.getId() );
			map.put( "status", HttpStatus.OK.value() );
		}
		catch( Exception e ) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body( map );
		}
		return ResponseEntity.ok( map );
	}
}
