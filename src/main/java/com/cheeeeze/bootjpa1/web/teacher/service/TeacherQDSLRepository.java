package com.cheeeeze.bootjpa1.web.teacher.service;

import java.util.List;
import java.util.function.Supplier;

import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherCnd;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherDTO;
import com.cheeeeze.bootjpa1.web.teacher.domain.TeacherInfo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.cheeeeze.bootjpa1.web.teacher.domain.QTeacherInfo.teacherInfo;

/**
 * @Description : 교사 QueryDSL
 * @Date        : 2024. 11. 1.
 **/
@Repository
@RequiredArgsConstructor
public class TeacherQDSLRepository {
	
	private final JPAQueryFactory queryFactory;
	
	public List<TeacherDTO> getPageList( TeacherCnd cnd ) {
		
		List<TeacherInfo> rtList = queryFactory.selectFrom( teacherInfo )
											   .where(
														   nameContain( cnd.getName() )
											   )
											   .orderBy( teacherInfo.inputDate.desc() )
											   .offset( cnd.getOffset() )
											   .limit( cnd.getSize() )
											   .fetch();
		
		List<TeacherDTO> list = rtList.stream().map( TeacherDTO::fromEntity ).toList();
		
		return list;
	}
	
	public Long getJpaQueryInfo( TeacherCnd cnd ) {
		return queryFactory
						   .select( teacherInfo.count() )
						   .from( teacherInfo )
						   .where(
									   nameContain( cnd.getName() )
						   ).fetchOne();
	}
	
	private BooleanBuilder nameContain( String userName ) {
		return nullSafeBuilder( () -> teacherInfo.name.contains( userName ) );
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
