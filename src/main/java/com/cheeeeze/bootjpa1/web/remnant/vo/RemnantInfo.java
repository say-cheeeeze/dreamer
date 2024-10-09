package com.cheeeeze.bootjpa1.web.remnant.vo;

import com.cheeeeze.bootjpa1.web.util.BaseTimeEntity;
import com.cheeeeze.bootjpa1.web.util.Gender;
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
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Builder
	public RemnantInfo( Long id, String name, String grade, Gender gender ) {
		this.id = id;
		this.name = name;
		this.grade = grade;
		this.gender = gender;
	}
	
	@Override public String toString() {
		return "RemnantInfo{" +
			   "id=" + id +
			   ", name='" + name + '\'' +
			   ", grade='" + grade + '\'' +
			   ", inputDate='" + getInputDate() + '\'' +
			   ", updateDate='" + getUpdateDate() + '\'' +
			   ", gender=" + gender +
			   '}';
	}
	
	public void updateRemnantInfo( String name, String grade, Gender gender ) {
		this.name = name;
		this.grade = grade;
		this.gender = gender;
	}
}
