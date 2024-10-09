package com.cheeeeze.bootjpa1.web.remnant.service;

import java.util.List;

import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.vo.RemnantInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RemnantService {
	
	private final RemnantInfoRepository repository;
	
	public RemnantInfo saveRemnant( RemnantInfo info ) {
		return repository.save( info );
	}
	
	public RemnantInfo getRemnantInfo( RemnantCnd cnd ) {
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
	
	public List<RemnantInfo> getRemnantListByName( RemnantCnd cnd ) {
		Page<RemnantInfo> pageInfo = repository.findByName( cnd.getName(), PageRequest.of( cnd.getPage(), cnd.getSize(), Sort.Direction.DESC, "inputDate" ) );
		List<RemnantInfo> content = pageInfo.getContent();
		return content;
	}
	
}
