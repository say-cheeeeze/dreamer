package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static com.cheeeeze.bootjpa1.web.remnant.vo.QRemnantInfo.remnantInfo;

/**
 * @methodName   : QueryDSL 을 활용하는 Repository
 * @author       : 남윤재
 * @date         : 2024-10-09
 * @description  : 
 */
@Repository
@RequiredArgsConstructor
public class RemnantQDSLRepository {
	
	private final JPAQueryFactory queryFactory;
	
	public List<RemnantInfo> getPageList( RemnantCnd cnd ) {
		
		List<RemnantInfo> rtList = queryFactory
											   .selectFrom( remnantInfo )
											   .where(
														   nameContain( cnd.getName() )
																	   .or( gradeEqual( cnd.getGrade() ) )
											   )
											   .orderBy( remnantInfo.inputDate.desc() )
											   .offset( cnd.getOffset() )
											   .limit( cnd.getSize() )
											   .fetch();
		
		return rtList;
	}
	
	public Long getJpaQueryInfo( RemnantCnd cnd ) {
		return queryFactory
						   .select( remnantInfo.count() )
						   .from( remnantInfo )
						   .where(
									   nameContain( cnd.getName() )
												   .or( gradeEqual( cnd.getGrade() ) )
						   ).fetchOne();
	}
	
	private BooleanBuilder nameContain( String userName ) {
		return nullSafeBuilder( () -> remnantInfo.name.contains( userName ) );
	}
	
	private BooleanBuilder gradeEqual( String grade ) {
		return nullSafeBuilder( () -> remnantInfo.grade.eq( grade ) );
	}
	
	public BooleanBuilder nullSafeBuilder( Supplier<BooleanExpression> f ) {
		try {
			return new BooleanBuilder( f.get() );
		}
		catch ( Exception e ) {
			return new BooleanBuilder();
		}
	}
}
