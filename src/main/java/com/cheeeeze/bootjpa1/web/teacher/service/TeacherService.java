package com.cheeeeze.bootjpa1.web.teacher.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherCnd;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherDTO;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService {
	
	private final TeacherInfoRepository teacherRepository;
	private final TeacherQDSLRepository teacherQDSLRepository;
	
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
	
	public Optional<TeacherInfo> findByLoginIdAndPassword( String loginId, String password ) {
		return teacherRepository.findByLoginIdAndPassword( loginId, password );
	}
	
	public Map<String, Object> getTeacherPageListByCnd( TeacherCnd cnd ) {
		
		List<TeacherDTO> rtList = teacherQDSLRepository.getPageList( cnd );
		Long totalCount = teacherQDSLRepository.getJpaQueryInfo( cnd );
		
		Page<TeacherDTO> page = new PageImpl<>( rtList, cnd.getPageable(), totalCount );
		Map<String, Object> map = new HashMap<>();
		map.put( "list", rtList );
		map.put( "isLast", page.isLast() );
		map.put( "isFirst", page.isFirst() );
		map.put( "hasNext", page.hasNext() );
		map.put( "hasPrevious", page.hasPrevious() );
		map.put( "pageSize", page.getSize() );
		map.put( "totalCount", totalCount );
		return map;
	}
}
