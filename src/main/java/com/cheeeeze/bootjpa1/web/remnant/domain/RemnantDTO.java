package com.cheeeeze.bootjpa1.web.remnant.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageDTO;
import com.cheeeeze.bootjpa1.web.util.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description : Remnant Data Transfer Object
 *
 * @Date        : 2024. 10. 17.
 * @class       : RemnantDTO
 **/
@Data
@NoArgsConstructor
public class RemnantDTO {
	
	private Long id;
	private String name;
	private String grade;
	private Gender gender;
	private ImageDTO imageDto;
	private LocalDateTime inputDate;
	private LocalDateTime updateDate;
	
	public RemnantInfo toRemnantEntity() {
		RemnantInfo.RemnantInfoBuilder builder = RemnantInfo.builder();
		builder.id( this.id );
		builder.name( this.name );
		builder.grade( this.grade );
		builder.gender( this.gender );
		builder.inputDate( this.inputDate );
		builder.updateDate( this.updateDate );
		return builder.build();
	}
}
