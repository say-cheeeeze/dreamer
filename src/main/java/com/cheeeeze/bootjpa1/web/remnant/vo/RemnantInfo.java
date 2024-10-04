package com.cheeeeze.bootjpa1.web.user.vo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table( name = "REMNANT")
public class RemnantInfo {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	private String name;
	private String grade;
	private LocalDateTime inputDate;
	private LocalDateTime updateDate;
	
	public Long getId() {
		return id;
	}
	
	public void setId( Long id ) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName( String name ) {
		this.name = name;
	}
	
	public String getGrade() {
		return grade;
	}
	
	public void setGrade( String grade ) {
		this.grade = grade;
	}
	
	@Override public String toString() {
		return "RemnantInfo{" +
			   "id=" + id +
			   ", name='" + name + '\'' +
			   ", grade='" + grade + '\'' +
			   '}';
	}
}
