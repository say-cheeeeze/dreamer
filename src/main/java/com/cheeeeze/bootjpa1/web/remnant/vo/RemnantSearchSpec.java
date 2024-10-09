package com.cheeeeze.bootjpa1.web.remnant.vo;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.util.Gender;
import org.springframework.data.jpa.domain.Specification;

/**
 * @methodName   : 다중검색을 위해 설정. JpaSpecificationExecutor 을 상속받은 repository 에서 사용.
 * @author       : 남윤재
 * @date         : 2024-10-08
 * @description  :
 */
public class RemnantSearchSpec {
	
	// name like
	public static Specification<RemnantInfo> equalsName( String name ) {
		return ( root, query, criteriaBuilder ) -> criteriaBuilder.like( root.get( "name" ), "%" + name + "%" );
	}
	
	// gender equal
	public static Specification<RemnantInfo> equalsGender( Gender gender ) {
		return ( root, query, criteriaBuilder ) -> criteriaBuilder.equal( root.get( "gender" ), gender.toString() );
	}
	
	// 날짜 범위
	public static Specification<RemnantInfo> betweenInputDate( LocalDateTime startDate, LocalDateTime endDate ) {
		return ( root, query, criteriaBuilder ) -> criteriaBuilder.between( root.get( "inputDate" ), startDate, endDate );
	}
}
