package com.cheeeeze.bootjpa1.web.remnant.service;

import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemnantRepository extends JpaRepository<RemnantInfo, Long> {


}
