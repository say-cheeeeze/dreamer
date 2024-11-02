package com.cheeeeze.bootjpa1.web.remnant.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageDTO;
import com.cheeeeze.bootjpa1.web.util.Gender;
import jakarta.persistence.Column;
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
	private String birth;
	
	private String school;
	private String roadAddr;
	private String jibunAddr;
	private String zoneCode;
	private String fullAddr;
	
	private String etcAddr;
	private String friend;
	private String history;
	private LocalDateTime inputDate;
	private LocalDateTime updateDate;
	
	private ImageDTO imageDto;
	
	public RemnantInfo toRemnantEntity() {
		RemnantInfo.RemnantInfoBuilder builder = RemnantInfo.builder();
		builder.id( this.id );
		builder.name( this.name );
		builder.grade( this.grade );
		builder.gender( this.gender );
		builder.birth( this.birth );
		
		builder.school( this.school );
		builder.roadAddr( this.roadAddr );
		builder.jibunAddr( this.jibunAddr );
		builder.zoneCode( this.zoneCode );
		builder.fullAddr( this.fullAddr );
		builder.etcAddr( this.etcAddr );
		
		builder.friend( this.friend );
		builder.history( this.history );
		builder.inputDate( this.inputDate );
		builder.updateDate( this.updateDate );
		return builder.build();
	}
}
