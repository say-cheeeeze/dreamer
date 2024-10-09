package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.ArrayList;
import java.util.List;

import static com.cheeeeze.bootjpa1.web.remnant.vo.QRemnantInfo.remnantInfo;

import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * @methodName   : QueryDSL 을 활용하는 Repository
 * @author       : 남윤재
 * @date         : 2024-10-09
 * @description  : 
 */
@Repository
@RequiredArgsConstructor
public class RemnantQDSLRepostiory {
	
	private final JPAQueryFactory queryFactory;
	
	public List<RemnantInfo> getRemnantList( RemnantCnd cnd ) {
		List<RemnantInfo> rtList = queryFactory
											   .selectFrom( remnantInfo )
											   .where(
														   nameContain( cnd.getName() ),
														   gradeEqual( cnd.getGrade() )
											   )
											   .orderBy( remnantInfo.inputDate.desc() )
											   .offset( cnd.getPage() )
											   .limit( cnd.getSize() )
											   .fetch();
		
		return rtList;
	}
	
	public Long getCount( RemnantCnd cnd ) {
		Long count = queryFactory
								 .select( remnantInfo.count() )
								 .from( remnantInfo )
								 .where(
											 nameContain( cnd.getName() ),
											 gradeEqual( cnd.getGrade() )
								 ).fetchOne();
		return count;
	}
	
	//  BooleanExpression은 and, or 을 조합해서 새로운 BooleanExpression을 만들 수 있다.
	//  또한, 결과 값이 null일 경우 무시하기 때문에 npe를 방지할 수 있다
	private BooleanExpression nameContain( String userName ) {
		return StringUtils.hasText( userName ) ? remnantInfo.name.contains( userName ) : null;
	}
	
	private BooleanExpression gradeEqual( String grade ) {
		return StringUtils.hasText( grade ) ? remnantInfo.grade.eq( grade ) : null;
	}
	
}
