package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class RemnantService {
	
	private final RemnantInfoRepository repository;
	private final RemnantQDSLRepository qdslRepository;
	
	public RemnantInfo saveRemnant( RemnantInfo info ) {
		
		if ( !StringUtils.hasText( info.getName() ) ||
			 !StringUtils.hasText( info.getGrade() ) ||
			 null == info.getGender() ) {
			
			throw new RuntimeException( "필수값 누락" );
		}
		
		if ( info.getId() != null ) {
			RemnantInfo remnantInfoById = getRemnantInfoById( info.getId() );
			remnantInfoById.updateRemnantInfo( info.getName(), info.getGrade(), info.getGender() );
			return repository.save( remnantInfoById );
		}
		
		return repository.save( info );
	}
	
	public RemnantInfo getRemnantInfoById( Long id ) {
		return repository.findById( id ).orElse( null );
	}
	
	public RemnantInfo getRemnantInfo( RemnantCnd cnd ) {
		
		if ( null == cnd.getId() ) {
			throw new RuntimeException( "필수값 누락" );
		}
		return repository.findById( cnd.getId() ).orElse( null );
	}
	
	public List<RemnantInfo> getRemnantListAll() {
		return repository.findAll();
	}
	
	public Page<RemnantInfo> getRemnantPageList( int page, int size ) {
		RemnantCnd cnd = new RemnantCnd();
		cnd.setPage( page );
		cnd.setSize( size );
		return getRemnantList( cnd );
	}
	
	public Page<RemnantInfo> getRemnantList( RemnantCnd cnd ) {
		return repository.findAll( PageRequest.of( cnd.getPage(), cnd.getSize(), Sort.Direction.DESC, "inputDate" ) );
	}
	
	public void deleteRemnantById( long id ) {
		repository.deleteById( id );
	}
	
	public Map<String, Object> getRemnantPageListByCnd( RemnantCnd cnd ) {
		
		List<RemnantInfo> rtList = qdslRepository.getPageList( cnd );
		Long totalCount = qdslRepository.getJpaQueryInfo( cnd );
		
		Page<RemnantInfo> page = new PageImpl<>( rtList, cnd.getPageable(), totalCount );
		Map<String, Object> map = new HashMap<>();
		map.put( "list", rtList );
		map.put( "isLast", page.isLast() );
		map.put( "isFirst", page.isFirst() );
		map.put( "hasNext", page.hasNext() );
		map.put( "hasPrevious", page.hasPrevious() );
		map.put( "pageSize", page.getSize() );
		map.put( "totalCount", totalCount );
		return map;
	}
	
}
