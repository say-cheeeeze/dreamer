package com.cheeeeze.bootjpa1.web.teacher.service;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherInfoRepository extends JpaRepository<TeacherInfo, Long> {

}