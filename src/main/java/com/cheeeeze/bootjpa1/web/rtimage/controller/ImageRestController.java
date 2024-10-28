package com.cheeeeze.bootjpa1.web.rtimage.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping( "/api/image/" )
public class ImageRestController {
	
	@Value( "${image.root_dir}" )
	private String imgRootDir;
	
	@Value( "${image.build_dir}" )
	private String imgBuildDir;
	
	/**
	 * @Description : image 실제 경로를 반환해줍니다.
	 * @Date        : 2024. 10. 21.
	 * @class       : ImageRestController
	 **/
	@Deprecated
	@PostMapping( "getImageFullPath" )
	public ResponseEntity<?> getImageFullPath( @RequestBody Map<String, Object> paramMap ) {
		try {
			String filePath = String.valueOf( paramMap.get( "filePath" ) );
			String fileName = String.valueOf( paramMap.get( "fileName" ) );
			
			filePath = filePath.replaceAll( fileName, "" );
			System.out.println( "fileName = " + fileName );
			System.out.println( "filePath = " + filePath );
			
			// 실제 경로에 해당 파일이 있는지 확인
			Path realPath = Paths.get( imgRootDir + filePath ).resolve( fileName ).normalize();
			Resource resource = new UrlResource( realPath.toUri() );
			
			log.info( "imgRootDir => {}", imgRootDir );
			log.info( "resource => {}", resource.getURI() );
			log.info( "filePath => {}", filePath );
			
			if ( resource.exists() ) {
				return ResponseEntity.ok()
									 .header( HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"" )
									 .body( resource );
			}
			else {
				return ResponseEntity.notFound().build();
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().build();
		}
	}
}
