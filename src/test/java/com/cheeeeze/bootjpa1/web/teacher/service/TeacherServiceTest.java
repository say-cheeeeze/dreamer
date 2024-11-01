package com.cheeeeze.bootjpa1.web.teacher.service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherCnd;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherDTO;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles( "test" )
class TeacherServiceTest {
	
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TeacherInfoRepository teacherRepository;
	
	@Test
	void teacherInsert_TEST() {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setName( "test" );
		teacherDTO.setEmail( "test@test.com" );
		teacherDTO.setPassword( "test" );
		teacherDTO.setLoginId( "loginId" );
		teacherDTO.setPhone( "01086449349" );
		TeacherInfo teacherInfo = teacherDTO.toTeacherInfo();
		teacherRepository.save( teacherInfo );
	}
	
	@Test
	void teacherUpdate_TEST() {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setName( "test" );
		teacherDTO.setEmail( "test@test.com" );
		teacherDTO.setPassword( "test" );
		teacherDTO.setLoginId( "loginId" );
		teacherDTO.setPhone( "01086449349" );
		TeacherInfo teacherInfo = teacherDTO.toTeacherInfo();
		TeacherInfo save = teacherRepository.save( teacherInfo );
		
		TeacherInfo findTeacher = teacherRepository.findById( save.getId() ).orElse( null );
		TeacherDTO teacherDTO1 = findTeacher.toTeacherDTO();
		teacherDTO1.setName( "modi_name" );
		teacherDTO1.setEmail( "test11111@test.com" );
		TeacherInfo teacherInfo1 = teacherDTO1.toTeacherInfo();
		TeacherInfo save1 = teacherRepository.save( teacherInfo1 );
		
		Assertions.assertThat( Objects.equals( save1.getId(), save.getId() ) ).isTrue();
	}
	
	@Test
	void delete_Test() {
		TeacherDTO teacherDTO = new TeacherDTO();
		teacherDTO.setId( 1L );
		TeacherInfo teacherInfo = teacherDTO.toTeacherInfo();
		teacherRepository.delete( teacherInfo );
		
		Assertions.assertThat( teacherRepository.existsById( 1L ) ).isFalse();
	}
	
	@Test
	void delete_Test2() {
		teacherRepository.deleteById( 3L );
		Assertions.assertThat( teacherRepository.existsById( 3L ) ).isFalse();
	}
	
	@Test
	@DisplayName( "ID중복찾기 테스트" )
	void getByName_TEST() {
		String userId = "nyj9349";
		Optional<TeacherInfo> byLoginId1 = teacherRepository.findByLoginId( userId );
	}
	
	@Test
	@DisplayName( "교사 페이징 목록" )
	void pagingList_TEST() {
		TeacherCnd cnd = new TeacherCnd();
		Map<String, Object> teacherPageListByCnd = teacherService.getTeacherPageListByCnd( cnd );
		System.out.println( "teacherPageListByCnd = " + teacherPageListByCnd );
		
	}
}