package com.cheeeeze.bootjpa1.web.remnant.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.util.Gender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class RemnantCnd {
	
	private Long id;
	private String name;
	private String grade;
	private Gender gender;
	private LocalDateTime inputDate;
	private int page = 1;
	private int size = 10;
	
	public RemnantCnd() {
	}
	
	// mysql paging offset parameter
	public int getOffset() {
		int offset = 0;
		if ( this.page > 1 ) {
			offset = ( this.page - 1 ) * this.size;
		}
		return offset;
	}
	
	public Pageable getPageable() {
		return PageRequest.of( this.page - 1, this.size, Sort.by("inputDate").descending());
	}
}
