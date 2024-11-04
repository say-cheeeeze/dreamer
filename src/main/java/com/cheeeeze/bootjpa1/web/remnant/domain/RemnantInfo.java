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
	@Column( nullable = false )
	private String name;
	@Column( nullable = false )
	private String grade;
	@Enumerated(EnumType.STRING)
	@Column( nullable = false )
	private Gender gender;
	@Column( nullable = false )
	private String birth;
	
	/* 필수여부가 아닌 것들 */
	private String phone;
	private String school;
	private String roadAddr;
	private String jibunAddr;
	private String zoneCode;
	private String fullAddr;
	private String favorite;
	
	private String etcAddr;
	private String friend;
	@Lob
	private String history;
	@Column( name = "input_date", nullable = false )
	private LocalDateTime inputDate;
	@Column( name = "update_date", nullable = false )
	private LocalDateTime updateDate;
	
	// imageInfo 와 1:1 연관관계이고
	// cascade : 나, 즉 remnantInfo 가 지워질때 -> image 도 지우겠다. ( 부모가 삭제될 때 )
	// orphanRemoval : 고아객체 삭제 여부 -> 고아 이미지 객체가 생기면 그것도 지우겠다.( 연관관계가 깨지는 것을 지우겠다. )
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
	public RemnantInfo( Long id, String name, String grade, Gender gender,
						String birth, String school, String roadAddr, String jibunAddr, String phone,
						String zoneCode, String fullAddr, String etcAddr, String friend, String history, String favorite,
						ImageInfo imageInfo, LocalDateTime inputDate, LocalDateTime updateDate ) {
		this.id = id;
		this.name = name;
		this.grade = grade;
		this.gender = gender;
		this.phone = phone;
		this.imageInfo = imageInfo;
		this.birth = birth;
		this.school = school;
		this.favorite = favorite;
		this.roadAddr = roadAddr;
		this.jibunAddr = jibunAddr;
		this.zoneCode = zoneCode;
		this.fullAddr = fullAddr;
		this.etcAddr = etcAddr;
		this.friend = friend;
		this.history = history;
		this.inputDate = inputDate;
		this.updateDate = updateDate;
	}
	
	public void setImageInfo( ImageInfo imageInfo ) {
		this.imageInfo = imageInfo;
	}
	
	public void updateRemnantInfo( RemnantDTO remnantDTO ) {
		this.name = remnantDTO.getName();
		this.grade = remnantDTO.getGrade();
		this.gender = remnantDTO.getGender();
		this.birth = remnantDTO.getBirth();
		this.school = remnantDTO.getSchool();
		
		this.phone = remnantDTO.getPhone();
		this.favorite = remnantDTO.getFavorite();
		this.roadAddr = remnantDTO.getRoadAddr();
		this.jibunAddr = remnantDTO.getJibunAddr();
		this.zoneCode = remnantDTO.getZoneCode();
		
		this.fullAddr = remnantDTO.getFullAddr();
		this.etcAddr = remnantDTO.getEtcAddr();
		this.friend = remnantDTO.getFriend();
		this.history = remnantDTO.getHistory();
	}
	
	public RemnantDTO toRemnantDTO() {
		RemnantDTO remnantDTO = new RemnantDTO();
		remnantDTO.setId( this.id );
		remnantDTO.setName( this.name );
		remnantDTO.setGrade( this.grade );
		remnantDTO.setGender( this.gender );
		remnantDTO.setBirth( this.birth );
		
		remnantDTO.setFavorite( this.favorite );
		remnantDTO.setPhone( this.phone );
		remnantDTO.setSchool( this.school );
		remnantDTO.setRoadAddr( this.roadAddr );
		remnantDTO.setJibunAddr( this.jibunAddr );
		remnantDTO.setZoneCode( this.zoneCode );
		remnantDTO.setFullAddr( this.fullAddr );
		
		remnantDTO.setEtcAddr( this.etcAddr );
		remnantDTO.setFriend( this.friend );
		remnantDTO.setHistory( this.history );
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
