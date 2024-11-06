package com.cheeeeze.bootjpa1.web.teacher.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.base.Authority;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table( name = "TEACHER" )
@NoArgsConstructor
public class TeacherInfo {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	@Column( nullable = false, length = 10 )
	private String name;
	@Column( nullable = false, unique = true, length = 20, updatable = false )
	private String loginId;
	@Column( nullable = false, length = 100 )
	private String password;
	@Column( length = 30 )
	private String email;
	@Column( length = 11 )
	private String phone;
	@Column( name = "input_date", nullable = false )
	private LocalDateTime inputDate;
	@Column( name = "update_date", nullable = false )
	private LocalDateTime updateDate;
	@Enumerated( EnumType.STRING )
	private Authority authority;
	
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
	public TeacherInfo( Long id, String name, String loginId, String password, String email, String phone,
						Authority authority, LocalDateTime inputDate, LocalDateTime updateDate ) {
		this.id = id;
		this.name = name;
		this.loginId = loginId;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.inputDate = inputDate;
		this.updateDate = updateDate;
		this.authority = authority;
	}
	
}
