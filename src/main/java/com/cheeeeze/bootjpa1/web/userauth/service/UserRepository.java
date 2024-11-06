package com.cheeeeze.bootjpa1.web.userauth.service;

import java.util.Optional;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<TeacherInfo, Long> {
	Optional<TeacherInfo> findByLoginId( String loginId );
	boolean existsByLoginId( String loginId );
}
