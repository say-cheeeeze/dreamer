package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.remnant.domain.QRemnantInfo;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantInfo;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageInfo;
import com.cheeeeze.bootjpa1.web.rtimage.service.ImageInfoRepository;
import com.cheeeeze.bootjpa1.web.util.Gender;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles( "test" )
class RemnantRepositoryTest {
	
	private final QRemnantInfo qRemnantInfo = QRemnantInfo.remnantInfo;
	
	@Autowired
	private RemnantInfoRepository remnantRepository;
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	@Autowired
	private ImageInfoRepository imageRepository;
	
	
	@Test
	public void saveRemnantTest() {
		
		for ( int i = 0; i < 150; i++ ) {
			
			Gender gender;
			if ( i % 2 == 0 ) {
				gender = Gender.MALE;
			}
			else {
				gender = Gender.FEMALE;
			}
			
			RemnantDTO remDto = new RemnantDTO();
			remDto.setGender(gender);
			remDto.setName( "member" + i );
			remDto.setGrade( "6" );
			RemnantInfo remInfo = remDto.toRemnantEntity();
			
			RemnantInfo save = remnantRepository.save( remInfo );
			System.out.println( "save = " + save.getId() );
		}
		
	}
	
	
	@Test
	public void getRemnantInfoById_TEST() {
		
		Optional<RemnantInfo> byId = remnantRepository.findById( 999L );
		System.out.println( byId.isEmpty() );
		if ( byId.isPresent() ) {
			RemnantInfo remnantInfo = byId.get();
			System.out.println( "remnantInfo.getName() = " + remnantInfo.getName() );
		}
	}

	
	@Test
	public void getRemnantListAll_TEST() {
		List<RemnantInfo> rtListAll = remnantRepository.findAll();
		
		System.out.println( rtListAll.size() );
	}
	
	
	
	@Test
	public void existById_TEST() {
		boolean exist = remnantRepository.existsById( 99L );
		System.out.println( "exist = " + exist );
	}
	
	@Test
	public void getListById_TEST() {
		List<Long> idList = new ArrayList<>();
		idList.add( 9L );
		idList.add( 10L );
		
		List<RemnantInfo> allById = remnantRepository.findAllById( idList );
		System.out.println( allById.size() );
	}
	
	@Test
	public void deleteById_TEST() {
		long id = 409L;
		remnantRepository.deleteById( id );
		
		Optional<RemnantInfo> byId = remnantRepository.findById( id );
		Assertions.assertThat( byId ).isEmpty();
	}
	
	
	@Test
	public void gender_TEST() {
		System.out.println( Gender.MALE );
	}
	
	@Test
	public void pageList_Test() {
		
		PageRequest pageRequest = PageRequest.of( 0, 10, Sort.Direction.DESC, "inputDate" );
		
		Page<RemnantInfo> page = remnantRepository.findAll( pageRequest );
		
		int totalPages = page.getTotalPages();
		System.out.println( "totalPages = " + totalPages );
		
		System.out.println( "page.getTotalElements() = " + page.getTotalElements() );
		
		for ( RemnantInfo remnantInfo : page ) {
			System.out.println( remnantInfo.getId() + remnantInfo.getName() + " / " + remnantInfo.getInputDate() );
		}
	}
	
	
	@Test
	void pageListTEST() {
		Page<RemnantInfo> pageInfo = remnantRepository.findByNameContaining( "5", PageRequest.of( 0, 10 ) );
		
		pageInfo.getContent().forEach( System.out::println );
	}
	
	@Test
	void pageListTEST2() {
		// 검색어1개와 페이징처리.
		int page = 0;
		int size = 10;
		
		Page<RemnantInfo> byNameOrId = remnantRepository.findByNameOrId( "윤재", 1L, PageRequest.of( page, size, Sort.Direction.DESC, "inputDate" ) );
		List<RemnantInfo> content = byNameOrId.getContent();
		content.forEach( System.out::println );
	}
	
	@Test
	void pageListSearch() {
		RemnantCnd cnd = new RemnantCnd();
		cnd.setName( "1" );
		cnd.setGrade( "5" );
		cnd.setPage( 1 );
		
		List<RemnantInfo> mainList = jpaQueryFactory
												 .selectFrom( qRemnantInfo )
												 .where(
															 qRemnantInfo.name.contains( cnd.getName() )
												 )
												 .orderBy( qRemnantInfo.inputDate.desc() )
												 .offset( cnd.getPage() )
												 .limit( cnd.getSize() )
												 .fetch();
		
		JPAQuery<Long> resultLong = jpaQueryFactory.select( qRemnantInfo.count() )
												   .from( qRemnantInfo )
												   .where( qRemnantInfo.name.contains( cnd.getName() ) );
		
		Page<RemnantInfo> page = PageableExecutionUtils.getPage( mainList, cnd.getPageable(), resultLong::fetchOne );
		
		System.out.println( page );
		
		int totalPages = page.getTotalPages();
		System.out.println( "totalPages = " + totalPages );
		int number = page.getNumber();
		System.out.println( "number = " + number );
		long totalElements = page.getTotalElements();
		System.out.println( "totalElements = " + totalElements );
		boolean hasNext = page.hasNext();
		System.out.println( "hasNext = " + hasNext );
		boolean hasPrevious = page.hasPrevious();
		System.out.println( "hasPrevious = " + hasPrevious );
		boolean isFirst = page.isFirst();
		System.out.println( "isFirst = " + isFirst );
		boolean isLast = page.isLast();
		System.out.println( "isLast = " + isLast );
	}
	
	
	
	
	@Test
	void checkDeleteImageWhenDeleteRemnant() {
		
		RemnantInfo remnantInfo = remnantRepository.findById( 1L ).orElse( null );
		ImageInfo imageInfo = remnantInfo.getImageInfo();
		
		RemnantInfo remnantInfo1 = imageInfo.getRemnantInfo();
		
		System.out.println( "remnantInfo1 == remnantInfo = " + (remnantInfo1 == remnantInfo) );
		
	}
	
	@Test
	void orphanRemoval_TEST() {
		
		ImageInfo imageInfo = ImageInfo.builder().uploadFileName( "fileName" ).build();
		ImageInfo saveImage = imageRepository.save( imageInfo );
		
		RemnantInfo remnantInfo = RemnantInfo.builder().name( "test" ).grade( "1" ).gender( Gender.MALE ).build();
		remnantInfo.setImageInfo( saveImage );
		
		RemnantInfo saveRemnant = remnantRepository.save( remnantInfo );
		System.out.println( "saveRemnant = " + saveRemnant );
		
		// remnant 를 지웠을 때
		remnantRepository.deleteById( saveRemnant.getId() );
		// 이 때 image 도 지워진다.
		
		// 이미지도 없다.
		Assertions.assertThat( imageRepository.findById( saveRemnant.getId() ).orElse( null ) ).isNull();
		
	}
	
	@Test
	void orphanRemoval_TEST2() {
		
		ImageInfo imageInfo = ImageInfo.builder().uploadFileName( "fileName" ).build();
		ImageInfo saveImage = imageRepository.save( imageInfo );
		
		RemnantInfo remnantInfo = RemnantInfo.builder().name( "test" ).grade( "1" ).gender( Gender.MALE ).build();
		remnantInfo.setImageInfo( saveImage );
		
		RemnantInfo saveRemnant = remnantRepository.save( remnantInfo );
		System.out.println( "saveRemnant = " + saveRemnant );
		
		// 렘넌트에게서 해당 이미지값을 해제하면 image 도 삭제된다.
		RemnantInfo rtFindInfo = remnantRepository.findById( saveRemnant.getId() ).orElse( null );
		rtFindInfo.setImageInfo( null );
		
		remnantRepository.save( rtFindInfo );
		
		Assertions.assertThat( imageRepository.findById( saveImage.getId() ).orElse( null ) ).isNull();
	}
	
}