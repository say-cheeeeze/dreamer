package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RemnantRepositoryTest {
	
	@Autowired
	private RemnantRepository remnantRepository;
	
	@Test
	public void saveRemnantTest() {
		
		for ( int i = 0; i < 10; i++ ) {
			
			RemnantInfo remInfo = RemnantInfo.builder()
											 .sex( "남" )
											 .name( "남윤재" )
											 .grade( "5" ).build();
			
			System.out.println( "remInfo = " + remInfo );
			
			RemnantInfo save = remnantRepository.save( remInfo );
			System.out.println( "save = " + save.getId() );
		}
		
	}
	
	@Test
	public void getRemnantListAll_TEST() {
		List<RemnantInfo> rtListAll = remnantRepository.findAll();
		
		System.out.println( rtListAll.size() );
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
		long id = 19L;
		remnantRepository.deleteById( id );
		
		Optional<RemnantInfo> byId = remnantRepository.findById( id );
		Assertions.assertThat( byId ).isEmpty();
	}
}