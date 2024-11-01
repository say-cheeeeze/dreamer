package com.cheeeeze.bootjpa1.web.teacher.service;

import java.util.Optional;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherInfoRepository extends JpaRepository<TeacherInfo, Long> {

	Optional<TeacherInfo> findByLoginId( String userId );
	
	Optional<TeacherInfo> findByLoginIdAndPassword( String userId, String password );
}
