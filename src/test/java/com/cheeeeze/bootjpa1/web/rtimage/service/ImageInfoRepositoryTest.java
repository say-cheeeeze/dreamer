package com.cheeeeze.bootjpa1.web.rtimage.service;

import java.util.Optional;

import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageCnd;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageDTO;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles( "test ")
class ImageInfoRepositoryTest {
	
	@Autowired
	private ImageInfoRepository imageRepository;
	
	@Test
	void imageSave_TEST() {
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setFileSize( 1000L );
		imageDTO.setSaveFileName( "saveFileName.png" );
		imageDTO.setUploadFileName( "uploadFileName.png" );
		ImageInfo imageEntity = imageDTO.toImageEntity();
		ImageInfo save = imageRepository.save( imageEntity );
		System.out.println( "save = " + save );
	}
	
	@Test
	void getImage_TEST() {
		
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setFileSize( 1000L );
		imageDTO.setSaveFileName( "saveFileName.png" );
		imageDTO.setUploadFileName( "uploadFileName.png" );
		imageDTO.setFileFullPath( "/something/to/png/test.png" );
		ImageInfo imageEntity = imageDTO.toImageEntity();
		ImageInfo save = imageRepository.save( imageEntity );
		
		Optional<ImageInfo> byId = imageRepository.findById( save.getId() );
		assertTrue( byId.isPresent() );
		
		ImageInfo imageInfo = byId.get();
		System.out.println( "imageInfo = " + imageInfo );
	}
	
	@Test
	void list_Test() {
	}
	
	@Test
	void updateImage_TEST() {
		
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setFileSize( 1000L );
		imageDTO.setSaveFileName( "save1.png" );
		imageDTO.setUploadFileName( "upload1.png" );
		imageDTO.setFileFullPath( "/save1.png" );
		ImageInfo imageEntity = imageDTO.toImageEntity();
		ImageInfo save = imageRepository.save( imageEntity );
		
		Optional<ImageInfo> byId = imageRepository.findById( save.getId() );
		assertTrue( byId.isPresent() );
		
		ImageInfo imageInfo = byId.get();
		System.out.println( "imageInfo = " + imageInfo );
		
		imageInfo.updateImageInfo( imageDTO );
		ImageInfo save1 = imageRepository.save( imageInfo );
		
		System.out.println( "save1 = " + save1 );
	}
	
	@Test
	public void deleteImage_TEST() {
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setFileSize( 1000L );
		imageDTO.setSaveFileName( "save1.png" );
		imageDTO.setUploadFileName( "upload1.png" );
		imageDTO.setFileFullPath( "/save1.png" );
		ImageInfo imageEntity = imageDTO.toImageEntity();
		ImageInfo save = imageRepository.save( imageEntity );
		Optional<ImageInfo> byId = imageRepository.findById( save.getId() );
		assertTrue( byId.isPresent() );
		ImageInfo imageInfo = byId.get();
		System.out.println( "imageInfo = " + imageInfo );
		imageRepository.delete( imageInfo );
		Optional<ImageInfo> byId2 = imageRepository.findById( save.getId() );
		assertFalse( byId2.isPresent() );
		
	}
}