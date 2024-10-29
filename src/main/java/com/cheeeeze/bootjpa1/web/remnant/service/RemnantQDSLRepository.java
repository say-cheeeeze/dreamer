package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.List;
import java.util.function.Supplier;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.cheeeeze.bootjpa1.web.remnant.domain.QRemnantInfo.remnantInfo;

/**
 * @description  :
 * <pre>QueryDSL 을 활용하는 Repository,
 * 다중 검색, 페이징처리를 같이 할 수 있다.</pre>
 * @author       : 남윤재
 * @date         : 2024-10-09
 */
@Repository
@RequiredArgsConstructor
public class RemnantQDSLRepository {
	
	private final JPAQueryFactory queryFactory;
	
	public List<RemnantDTO> getPageList( RemnantCnd cnd ) {
		
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
		
		List<RemnantDTO> list = rtList.stream().map( RemnantInfo::toRemnantDTO ).toList();
		
		return list;
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
