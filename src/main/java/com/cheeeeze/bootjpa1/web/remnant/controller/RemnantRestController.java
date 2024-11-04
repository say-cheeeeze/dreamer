package com.cheeeeze.bootjpa1.web.remnant.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cheeeeze.bootjpa1.web.config.ObjectMapperConfig;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.remnant.service.RemnantService;
import com.cheeeeze.bootjpa1.web.rtimage.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping( "/api/remnant" )
public class RemnantRestController {
	
	private final RemnantService remnantService;
	private final ObjectMapper objectMapper;
	
	/**
	 * @description  : 단건 객체를 신규 등록 또는 수정합니다.
	 * @author       : 남윤재
	 * @date         : 2024-10-13
	 */
	@PostMapping( "/save" )
	public ResponseEntity<?> saveRemnant( @RequestParam( value = "file", required = false ) MultipartFile file,
										  @RequestParam( value = "data" ) String data ) {
		
		Map<String, Object> map = new HashMap<>();
		map.put( "status", HttpStatus.INTERNAL_SERVER_ERROR.value() );
		log.info( "data => {}\n/ file exist? => {} ", data, file != null );
		
		try {
			RemnantDTO rtDto = objectMapper.readValue( data, RemnantDTO.class );
			
			log.info( String.valueOf( rtDto ) );
			
			RemnantDTO remDto = remnantService.saveRemnantWithFile( rtDto, file );
			
			map.put( "saveInfo", remDto );
			map.put( "status", HttpStatus.OK.value() );
		}
		catch ( IOException e ) {
			log.error( "파일 저장 실패 / {}", e.getMessage() );
			return ResponseEntity.internalServerError().body( map );
		}
		catch ( Exception e ) {
			log.error( "데이터 저장 실패 / {}", e.getMessage() );
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
			RemnantDTO getInfo = remnantService.getRemnantDtoById( cnd );
			map.put( "info", getInfo );
			map.put( "status", HttpStatus.OK.value() );
		}
		catch ( Exception e ) {
			log.info( "렘넌트 조회 실패 / {}", e.getMessage() );
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
		catch ( Exception e ) {
			log.info( "렘넌트 삭제 실패 / {}", e.getMessage() );
			return ResponseEntity.internalServerError().body( map );
		}
		return ResponseEntity.ok( map );
	}
}
