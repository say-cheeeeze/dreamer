package com.cheeeeze.bootjpa1.web.remnant.vo;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@Table( name = "REMNANT")
public class RemnantInfo extends BaseTimeEntity {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	private String name;
	private String grade;
	private String sex;
	
	public void setSex( String sex ) {
		this.sex = sex;
	}
	
	public void setId( Long id ) {
		this.id = id;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public void setGrade( String grade ) {
		this.grade = grade;
	}
	
	@Builder
	public RemnantInfo( Long id, String name, String grade, String sex ) {
		this.id = id;
		this.name = name;
		this.grade = grade;
		this.sex = sex;
	}
}
