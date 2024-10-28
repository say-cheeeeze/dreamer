package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.List;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantInfo;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageDTO;
import com.cheeeeze.bootjpa1.web.rtimage.service.ImageInfoRepository;
import com.cheeeeze.bootjpa1.web.rtimage.service.ImageService;
import com.cheeeeze.bootjpa1.web.util.Gender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles( "test" )
public class RemnantServiceTest {
	
	@Autowired
	private RemnantService remnantService;
	@Autowired
	private ImageService imageService;
	@Autowired
	private ImageInfoRepository imageInfoRepository;
	@Autowired
	private RemnantInfoRepository remnantInfoRepository;
	
	@Test
	void saveRemnantTest() {
		
		RemnantDTO remDTO = new RemnantDTO();
		remDTO.setName( "남윤재" );
		remDTO.setGender( Gender.MALE );
		remDTO.setGrade( "6" );
		
		RemnantInfo remnantEntity = remDTO.toRemnantEntity();
		RemnantInfo save = remnantInfoRepository.save( remnantEntity );
		System.out.println( "save = " + save );
	}
	
	@Test
	public void getRemnantDtoByIdTEST() {
		RemnantCnd cnd = new RemnantCnd();
		cnd.setId( 1L );
		
		RemnantDTO getInfo = remnantService.getRemnantDtoById( cnd );
		System.out.println( "getInfo = " + getInfo );
	}
	
	@Test
	void setImageAtRemnant_TEST() {
		
		RemnantDTO remDTO = new RemnantDTO();
		remDTO.setName( "남윤재" );
		remDTO.setGender( Gender.MALE );
		remDTO.setGrade( "6" );
		
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setSaveFileName( "saveFileName" );
		imageDTO.setUploadFileName( "uploadFileName" );
		imageDTO.setFileSize( 1000L );
		imageDTO.setFileFullPath( "/.../server/app/test.png" );
		remDTO.setImageDto( imageDTO );
		
		RemnantDTO remnantDTO = remnantService.insertOrUpdate( remDTO );
		System.out.println( remnantDTO );
	}
	
	@Test
	void getRemnantListAll_serviceTEST() {
		List<RemnantDTO> remnantListAll = remnantService.getRemnantListAll();
		System.out.println( "remnantListAll = " + remnantListAll );
	}
	
	@Test
	void getRtPageList_TEST() {
		Page<RemnantDTO> remnantPageList = remnantService.getRemnantPageList( 0, 10 );
		
		System.out.println( "pageList = " + remnantPageList );
		for ( RemnantDTO remnantInfo : remnantPageList ) {
			System.out.println( remnantInfo );
		}
		
		System.out.println( remnantPageList.hasNext() ); // 다음 페이지 있냐
		System.out.println( remnantPageList.hasPrevious() ); // 이전페이지 있냐
		System.out.println( remnantPageList.getTotalPages() ); // 총 몇 페이지냐
		System.out.println( remnantPageList.getTotalElements() ); // 총 몇 개있냐
		System.out.println( remnantPageList.getNumber() ); // 현재 page number 뭐임
		System.out.println( remnantPageList.isFirst() ); // 맨 처음 페이지냐
		System.out.println( remnantPageList.isLast() ); // 맨 마지막 페이지냐
		
		// page 클래스는 getContent() 로 List 로 변환할 수 있다.
		List<RemnantDTO> content = remnantPageList.getContent();
	}
	
	@Test
	void deleteById_ServiceTest() {
		long id = 1L;
		remnantService.deleteRemnantById( id );
		
		RemnantCnd cnd = new RemnantCnd();
		cnd.setId( id );
		RemnantDTO mustNullInfo = remnantService.getRemnantDtoById( cnd );
		Assertions.assertThat( mustNullInfo ).isNull();
	}
	
	@Test
	void remnantAndImageCascadeDeleteTest() {
		
		RemnantDTO remDTO = new RemnantDTO();
		remDTO.setName( "남윤재" );
		remDTO.setGender( Gender.MALE );
		remDTO.setGrade( "6" );
		
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setSaveFileName( "saveFileName" );
		imageDTO.setUploadFileName( "uploadFileName" );
		imageDTO.setFileSize( 1000L );
		imageDTO.setFileFullPath( "/.../server/app/test.png" );
		remDTO.setImageDto( imageDTO );
		
		RemnantDTO remnantDTO = remnantService.insertRemnant( remDTO );
		Long rtId = remnantDTO.getId();
		Long imageId = remnantDTO.getImageDto().getId();
		
		remnantService.deleteRemnantById( rtId );
		
		ImageDTO imageDtoById = imageService.getImageDtoById( imageId );
		RemnantDTO remnantDtoById = remnantService.getRemnantDtoById( rtId );
		Assertions.assertThat( imageDtoById ).isNull();
		Assertions.assertThat( remnantDtoById ).isNull();
	}
	
	
	@Test
	void pageListSearch2() {
		
		RemnantCnd cnd = new RemnantCnd();
		//		cnd.setPage( 1 ); // 실제 1 page => offset : 0
		cnd.setPage( 2 ); // page : 2 offset : (2-1) * pageSize
		cnd.setName( "mem" );
		cnd.setGrade( "5" );
		//		cnd.setPage( 41 );
		Map<String, Object> resultMap = remnantService.getRemnantPageListByCnd( cnd );
		System.out.println( "totalCount : " + resultMap.get( "totalCount" ) );
		System.out.println( "isFirst : " + resultMap.get( "isFirst" ) );
		System.out.println( "isLast : " + resultMap.get( "isLast" ) );
		System.out.println( "hasNext : " + resultMap.get( "hasNext" ) );
		System.out.println( "hasPrevious : " + resultMap.get( "hasPrevious" ) );
		
	}
	
	
	@Test
	public void saveRemnantServiceTest() {
		RemnantDTO remDto = new RemnantDTO();
		remDto.setGender( Gender.FEMALE );
		remDto.setName( "남윤재" );
		remDto.setGrade( "6" );
		RemnantDTO remnantDTO = remnantService.insertRemnant( remDto );
		
		Assertions.assertThat( remnantDTO != null ).isTrue();
	}
	
	@Test
	void saveServiceTest() {
		RemnantInfo remInfo = RemnantInfo.builder()
										 .gender( Gender.MALE )
										 .name( "최종등록한다잉" )
										 .grade( "6" ).build();
		RemnantDTO remnantDTO = remnantService.insertRemnant( remInfo.toRemnantDTO() );
		
		System.out.println( "remnantInfo = " + remnantDTO );
		
	}
}
