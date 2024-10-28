package com.cheeeeze.bootjpa1.web.rtimage.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageDTO;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
	
	@Value( "${image.root_dir}" )
	private String imgRootDir;
	@Value( "${image.build_dir}" )
	private String imgBuildDir;
	
	private final ImageInfoRepository imageRepository;
	
	public ImageDTO save( ImageDTO imageDTO ) {
		
		ImageInfo save = imageRepository.save( imageDTO.toImageEntity() );
		return save.toImageDTO();
	}
	
	public ImageDTO getImageDtoById( Long id) {
		Optional<ImageInfo> byId = imageRepository.findById( id );
		return byId.map( ImageInfo::toImageDTO ).orElse( null );
	}
	
	public ImageDTO saveImageFile( MultipartFile file, Long remId ) throws IOException {
		
		if ( null == file || file.isEmpty() ) {
			return null;
		}
		
		String subDirPath = "rtimage" + File.separator + remId + File.separator;
		File saveDir = new File( imgRootDir + subDirPath );
		File buildDir = new File( imgBuildDir + subDirPath );
		if ( !saveDir.exists() ) {
			saveDir.mkdirs();
		}
		if ( !buildDir.exists() ) {
			buildDir.mkdirs();
		}
		
		String filenameExtension = StringUtils.getFilenameExtension( file.getOriginalFilename() ); // png
		
		if ( null == filenameExtension ) {
			throw new IOException("잘못된 형식입니다" );
		}
		
		String yyyyMMddHHmmss = LocalDateTime.now().format( DateTimeFormatter.ofPattern( "yyyyMMdd_HHmmss" ) );
		String saveFileName = yyyyMMddHHmmss.concat( "." ).concat( filenameExtension );
		InputStream fis = file.getInputStream();
		
		String fileFullPath = subDirPath + saveFileName;
		File imageFile = new File( imgRootDir + fileFullPath );
		Files.copy( fis, imageFile.toPath() );
		
		File backupFile = new File( imgBuildDir + fileFullPath );
		Files.copy( imageFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING );
		
		ImageDTO imgDto = new ImageDTO();
		imgDto.setFileFullPath( fileFullPath );
		imgDto.setSaveFileName( saveFileName );
		imgDto.setUploadFileName( file.getOriginalFilename() );
		imgDto.setFileSize( file.getSize() );
		
		return imgDto;
	}
	
	public ImageInfo updateImageByImageDto( ImageDTO imageDto ) {
		
		ImageInfo imageInfo = imageRepository.findById( imageDto.getId() )
								   .orElseThrow( () -> new RuntimeException( "등록된 이미지가 없습니다" ) );
		
		imageInfo.updateImageInfo( imageDto.getFileFullPath(),
								   imageDto.getSaveFileName(),
								   imageDto.getUploadFileName(),
								   imageDto.getFileSize()
		);
		return imageInfo;
	}
	
	public ImageInfo insertImageByImageDto( ImageDTO imageDto ) {
		return imageRepository.save( imageDto.toImageEntity() );
	}
}
