package com.cheeeeze.bootjpa1.web.base.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public abstract class SearchCnd {

	private int page = 1;
	private int size = 10;
	
	// mysql paging offset parameter
	public int getOffset() {
		int offset = 0;
		if ( this.page > 1 ) {
			offset = ( this.page - 1 ) * this.size;
		}
		return offset;
	}
	
	public Pageable getPageable() {
		return PageRequest.of( this.page - 1, this.size, Sort.by( "inputDate").descending());
	}
	
}
