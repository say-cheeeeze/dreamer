package com.cheeeeze.bootjpa1.web.remnant.service;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 */
@Repository
public interface RemnantInfoRepository extends JpaRepository<RemnantInfo, Long> {
	
	// 이름으로 like % % 검색. JPA :: By + Field명 + Containing
	Page<RemnantInfo> findByNameContaining( String name, Pageable pageable);
	
	// 이런 것도 있다.
	// List<RemnantInfo> findByNameContainingIgnoreCase(String name);
	
	Page<RemnantInfo> findByNameOrId( String name, Long id, Pageable pageable );
	
	@Query( "select r from RemnantInfo r where r.name like concat('%', :name, '%') " )
	Page<RemnantInfo> findByName( @Param("name") String name, Pageable pageable );
}
