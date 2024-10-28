package com.cheeeeze.bootjpa1.web.remnant.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageInfo;
import com.cheeeeze.bootjpa1.web.util.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description : Remnant JPA Entity
 * @Date        : 2024. 10. 17.
 * @class       : RemnantInfo
**/
@Getter
@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@Table( name = "REMNANT")
public class RemnantInfo {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	private String name;
	private String grade;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Column( name = "input_date" )
	private LocalDateTime inputDate;
	
	@Column( name = "update_date" )
	private LocalDateTime updateDate;
	
	// imageInfo 와 1:1 연관관계이고 remnantInfo 가 지워질때 -> image 도 지우겠다.
	// orphanRemoval 고아객체 삭제 여부 -> 고아 이미지 객체가 생기면 그것도 지우겠다.
	@OneToOne( cascade = CascadeType.REMOVE, orphanRemoval = true )
	@JoinColumn( name = "IMAGE_ID" )
	private ImageInfo imageInfo;
	
	@PrePersist
	void onPrePersist() {
		this.updateDate = LocalDateTime.now();
		this.inputDate = LocalDateTime.now();
	}
	
	@PreUpdate
	void onPreUpdate() {
		this.updateDate = LocalDateTime.now();
	}
	
	@Builder
	public RemnantInfo( Long id, String name, String grade, Gender gender, ImageInfo imageInfo, LocalDateTime inputDate, LocalDateTime updateDate ) {
		this.id = id;
		this.name = name;
		this.grade = grade;
		this.gender = gender;
		this.imageInfo = imageInfo;
		this.inputDate = inputDate;
		this.updateDate = updateDate;
	}
	
	public void setImageInfo( ImageInfo imageInfo ) {
		this.imageInfo = imageInfo;
	}
	
	public void updateRemnantInfo( String name, String grade, Gender gender ) {
		this.name = name;
		this.grade = grade;
		this.gender = gender;
	}
	
	public RemnantDTO toRemnantDTO() {
		RemnantDTO remnantDTO = new RemnantDTO();
		remnantDTO.setId( this.id );
		remnantDTO.setName( this.name );
		remnantDTO.setGrade( this.grade );
		remnantDTO.setGender( this.gender );
		remnantDTO.setInputDate( this.inputDate );
		remnantDTO.setUpdateDate( this.updateDate );
		if ( this.imageInfo != null ) {
			// 순환참조 방지!
			// 여기 remnant 는 image 를 가지도록 하되
			// image 에서는 자기 remnant 를 모르게 한다.
			remnantDTO.setImageDto( this.imageInfo.toImageDTO() );
		}
		return remnantDTO;
	}
}
