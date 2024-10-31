package com.cheeeeze.bootjpa1.web.teacher.service;

import java.util.Optional;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherDTO;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService {
	
	private final TeacherInfoRepository teacherRepository;
	
	public TeacherDTO insertTeacher( TeacherDTO teacherDTO ) {
		
		if ( essentialValidation( teacherDTO ) ) {
			throw new RuntimeException( "필수값 누락" );
		}
		
		TeacherInfo teacherInfo = teacherDTO.toTeacherInfo();
		TeacherInfo save = teacherRepository.save( teacherInfo );
		return save.toTeacherDTO();
	}
	
	private boolean essentialValidation( TeacherDTO teacherDTO ) {
		return !StringUtils.hasText( teacherDTO.getName() ) ||
			   !StringUtils.hasText( teacherDTO.getLoginId() ) ||
			   !StringUtils.hasText( teacherDTO.getPassword() );
	}
	
	public Optional<TeacherInfo> findByLoginId( String loginId ) {
		return teacherRepository.findByLoginId( loginId );
	}
}
