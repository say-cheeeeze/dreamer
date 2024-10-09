package com.cheeeeze.bootjpa1.web.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Getter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.processing.Pattern;

/**
 * @MappedSuperclass : 공통속성을 관리하는 객체로 지정하겠다. ( 이걸 활용하겠다. )
 * @PostLoad : 엔티티가 조회된 후
 * @PrePersist : 영속화되기 전
 * @PostPersist : 영속화된 후
 * @PreUpdate : 변경되기 전
 * @PostUpdate : 변경된 후
 * @PreRemove : 삭제되기 전
 * @PostRemove : 삭제된 후
 */
@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {
	
	@Column( name = "input_date", nullable = false )
	private LocalDateTime inputDate;
	
	@Column( name = "update_date", nullable = false )
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
