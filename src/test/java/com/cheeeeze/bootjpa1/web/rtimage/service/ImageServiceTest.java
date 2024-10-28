package com.cheeeeze.bootjpa1.web.rtimage.service;

import java.util.List;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.remnant.service.RemnantInfoRepository;
import com.cheeeeze.bootjpa1.web.remnant.service.RemnantService;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantInfo;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles( "test" )
class ImageServiceTest {
	
	@Autowired
	private ImageInfoRepository imageRepository;
	@Autowired
	private RemnantService remnantService;
	@Autowired
	private RemnantInfoRepository remnantRepository;
	
	
	@Test
	void orphanRemoval_TEST() {
		for ( int i = 0; i < 100; i++ ) {
			ImageInfo imageInfo = ImageInfo.builder().uploadFileName( "fileName" + i ).build();
			imageRepository.save( imageInfo );
		}
		
		List<RemnantInfo> all = remnantRepository.findAll();
		
		for ( RemnantInfo remnantInfo : all ) {
			remnantInfo.setImageInfo( null );
			remnantRepository.save( remnantInfo );
		}
	}
}