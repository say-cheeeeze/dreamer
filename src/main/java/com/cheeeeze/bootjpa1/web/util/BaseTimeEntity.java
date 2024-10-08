package com.cheeeeze.bootjpa1.web.util;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
	
	@Column( name = "input_date" )
	private LocalDateTime inputDate;
	
	@Column( name = "update_date" )
	private LocalDateTime updateDate;
	
	@PrePersist
	public void prePersist() {
		this.inputDate = LocalDateTime.now();
		this.updateDate = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		this.updateDate = LocalDateTime.now();
	}
}
