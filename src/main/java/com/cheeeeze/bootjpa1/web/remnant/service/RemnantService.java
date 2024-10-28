package com.cheeeeze.bootjpa1.web.remnant.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantCnd;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantDTO;
import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantInfo;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageDTO;
import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageInfo;
import com.cheeeeze.bootjpa1.web.rtimage.service.ImageInfoRepository;
import com.cheeeeze.bootjpa1.web.rtimage.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional( rollbackFor = Exception.class )
@RequiredArgsConstructor
public class RemnantService {
	
	private final RemnantInfoRepository remnantRepository;
	private final RemnantQDSLRepository qdslRepository;
	private final ImageInfoRepository imageRepository;
	private final ImageService imageService;
	
	public RemnantDTO insertOrUpdate( RemnantDTO remnantDto ) {
		
		if ( essentialValidation( remnantDto ) ) {
			throw new RuntimeException( "필수값 누락" );
		}
		if ( null == remnantDto.getId() || 0 == remnantDto.getId() ) {
			return insertRemnant( remnantDto );
		}
		else {
			return updateRemnant( remnantDto );
		}
	}
	
	/**
	 * Transactional Propagation.NESTED : 부모 트랜잭션이 있다면 Required ( = 부모 트랜잭션에 합류 )
	 * Required : 자식/부모 rollback . 발생된다면 자식 부모 모두 rollback 이 발생한다.
	 *
	 * -> JpaDialect does not support savepoints - check your JPA provider's capabilities 관련 에러가 나서
	 * 지원하지 않는 것으로 보인다 NESTED 는 JPA 와 함께 사용하지 말자
	 *
	 *
	 * @param remnantDto
	 * @return
	 */
	public RemnantDTO insertRemnant( RemnantDTO remnantDto ) {
		
		RemnantInfo remnantInfo = remnantDto.toRemnantEntity();
		
		if ( remnantDto.getImageDto() != null && remnantDto.getImageDto().getFileSize() > 0 ) {
			
			ImageInfo imageEntity = remnantDto.getImageDto().toImageEntity();
			imageRepository.save( imageEntity );
			remnantInfo.setImageInfo( imageEntity );
		}
		RemnantInfo save = remnantRepository.save( remnantInfo );
		return save.toRemnantDTO();
	}
	
	public RemnantDTO updateRemnant( RemnantDTO remnantDto ) {
		
		RemnantInfo remnantInfo = remnantRepository.findById( remnantDto.getId() )
												   .orElseThrow( () -> new RuntimeException( "등록된 렘넌트 정보가 없습니다" ) );
		
		remnantInfo.updateRemnantInfo( remnantDto.getName(), remnantDto.getGrade(), remnantDto.getGender() );
		
		// 요청 파라미터에 이미지가 있는 경우
		if ( remnantDto.getImageDto() != null && remnantDto.getImageDto().getFileSize() > 0 ) {
			
			ImageInfo imageInfo;
			if ( remnantDto.getImageDto().getId() != null ) {
				
				// 기존 이미지 수정
				imageInfo = imageService.updateImageByImageDto( remnantDto.getImageDto() );
				
			}
			// 신규 이미지
			else {
				imageInfo = imageService.insertImageByImageDto( remnantDto.getImageDto() );
			}
			
			remnantInfo.setImageInfo( imageInfo );
		}
		else {
			// 요청 파라미터에 이미지가 없는 경우
			remnantInfo.setImageInfo( null );
		}
		
		RemnantInfo save = remnantRepository.save( remnantInfo );
		return save.toRemnantDTO();
	}
	
	private boolean essentialValidation( RemnantDTO remnantDto ) {
		return !StringUtils.hasText( remnantDto.getName() ) ||
			   !StringUtils.hasText( remnantDto.getGrade() ) ||
			   null == remnantDto.getGender();
	}
	
	public RemnantDTO getRemnantDtoById( Long id ) {
		Optional<RemnantInfo> byId = remnantRepository.findById( id );
		return byId.map( RemnantInfo::toRemnantDTO ).orElse( null );
	}
	
	public RemnantDTO getRemnantDtoById( RemnantCnd cnd ) {
		
		if ( null == cnd.getId() ) {
			throw new RuntimeException( "필수값 누락" );
		}
		
		Optional<RemnantInfo> byId = remnantRepository.findById( cnd.getId() );
		return byId.map( RemnantInfo::toRemnantDTO ).orElse( null );
	}
	
	public List<RemnantDTO> getRemnantListAll() {
		List<RemnantInfo> all = remnantRepository.findAll();
		return all.stream().map( RemnantInfo::toRemnantDTO ).toList();
	}
	
	public Page<RemnantDTO> getRemnantPageList( int page, int size ) {
		RemnantCnd cnd = new RemnantCnd();
		cnd.setPage( page );
		cnd.setSize( size );
		return getRemnantList( cnd );
	}
	
	public Page<RemnantDTO> getRemnantList( RemnantCnd cnd ) {
		Page<RemnantInfo> infoList = remnantRepository.findAll( PageRequest.of( cnd.getPage(), cnd.getSize(), Sort.Direction.DESC, "inputDate" ) );
		Page<RemnantDTO> remnantDtoList = infoList.map( RemnantInfo::toRemnantDTO );
		return remnantDtoList;
	}
	
	public void deleteRemnantById( long id ) {
		remnantRepository.deleteById( id );
	}
	
	public Map<String, Object> getRemnantPageListByCnd( RemnantCnd cnd ) {
		
		List<RemnantDTO> rtList = qdslRepository.getPageList( cnd );
		Long totalCount = qdslRepository.getJpaQueryInfo( cnd );
		
		Page<RemnantDTO> page = new PageImpl<>( rtList, cnd.getPageable(), totalCount );
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
	
	public RemnantDTO saveRemnantWithFile( RemnantDTO rtDto, MultipartFile file ) throws IOException {
		
		RemnantDTO remDto = insertOrUpdate( rtDto );
		
		ImageDTO saveImageDto = imageService.saveImageFile( file, remDto.getId() );
		
		if ( saveImageDto != null ) {
			remDto.setImageDto( saveImageDto );
			updateRemnant( remDto );
		}
		
		return remDto;
	}
}
