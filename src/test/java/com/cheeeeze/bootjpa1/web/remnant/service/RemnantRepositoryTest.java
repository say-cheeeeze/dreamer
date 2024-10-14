package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.remnant.vo.QRemnantInfo;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
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
	
	@Autowired
	private RemnantInfoRepository remnantRepository;
	
	@Autowired
	private RemnantService remnantService;
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	private final QRemnantInfo qRemnantInfo = QRemnantInfo.remnantInfo;
	
	
	@Test
	public void saveRemnantTest() {
		
		for ( int i = 0; i < 400; i++ ) {
			
			Gender gender = null;
			if ( i % 2 == 0 ) {
				gender = Gender.MALE;
			}
			else {
				gender = Gender.FEMALE;
			}
			
			RemnantInfo remInfo = RemnantInfo.builder()
											 .gender( gender )
											 .name( "member" + i )
											 .grade( "6" ).build();
			
			RemnantInfo save = remnantRepository.save( remInfo );
			System.out.println( "save = " + save.getId() );
		}
		
	}
	
	@Test
	public void saveRemnantServiceTest() {
		RemnantInfo remInfo = RemnantInfo.builder()
										 .gender( Gender.MALE )
										 .name( "남윤재" )
										 .grade( "5" ).build();
		RemnantInfo saveInfo = remnantService.saveRemnant( remInfo );
		System.out.println( saveInfo );
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
	public void getRemnantInfoTEST() {
		RemnantCnd cnd = new RemnantCnd();
		cnd.setId( 815L );
		
		RemnantInfo getInfo = remnantService.getRemnantInfo( cnd );
		System.out.println( "getInfo = " + getInfo );
	}
	
	@Test
	public void getRemnantListAll_TEST() {
		List<RemnantInfo> rtListAll = remnantRepository.findAll();
		
		System.out.println( rtListAll.size() );
	}
	
	@Test
	void getRemnantListAll_serviceTEST() {
		List<RemnantInfo> remnantListAll = remnantService.getRemnantListAll();
		System.out.println( "remnantListAll = " + remnantListAll );
	}
	
	@Test
	void getRtPageList_TEST() {
		Page<RemnantInfo> pageList = remnantService.getRemnantPageList( 0, 10 );
		
		System.out.println( "pageList = " + pageList );
		for ( RemnantInfo remnantInfo : pageList ) {
			System.out.println( remnantInfo );
		}
		
		System.out.println( pageList.hasNext() ); // 다음 페이지 있냐
		System.out.println( pageList.hasPrevious() ); // 이전페이지 있냐
		System.out.println( pageList.getTotalPages() ); // 총 몇 페이지냐
		System.out.println( pageList.getTotalElements() ); // 총 몇 개있냐
		System.out.println( pageList.getNumber() ); // 현재 page number 뭐임
		System.out.println( pageList.isFirst() ); // 맨 처음 페이지냐
		System.out.println( pageList.isLast() ); // 맨 마지막 페이지냐
		
		// page 클래스는 getContent() 로 List 로 변환할 수 있다.
		List<RemnantInfo> content = pageList.getContent();
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
	void deleteById_ServiceTest() {
		long id = 19L;
		remnantService.deleteRemnantById( id );
		
		RemnantCnd cnd = new RemnantCnd();
		cnd.setId( 19L );
		RemnantInfo mustNullInfo = remnantService.getRemnantInfo( cnd );
		Assertions.assertThat( mustNullInfo ).isNull();
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
	void Test1() {
		System.out.println( Gender.valueOf( "MALE" ) );
		RemnantCnd remnantCnd = new RemnantCnd();
		remnantCnd.setId(80L);
		RemnantInfo findInfo = remnantService.getRemnantInfo( remnantCnd );
		System.out.println( findInfo );
		
		findInfo.updateRemnantInfo( findInfo.getName(), findInfo.getGrade(), Gender.MALE );
		remnantRepository.save( findInfo );
	}
	
	@Test
	void pageListTEST() {
		Page<RemnantInfo> pageInfo = remnantRepository.findByNameContaining( "5", PageRequest.of( 0, 10) );
		
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
	void saveServiceTest() {
		RemnantInfo remInfo = RemnantInfo.builder()
										 .gender( Gender.MALE )
										 .name( "최종등록한다잉" )
										 .grade( "6" ).build();
		RemnantInfo remnantInfo = remnantService.saveRemnant( remInfo );
		System.out.println( "remnantInfo = " + remnantInfo );
		
	}
	
}