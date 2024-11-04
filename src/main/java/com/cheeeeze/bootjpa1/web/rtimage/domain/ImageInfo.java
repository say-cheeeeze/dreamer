package com.cheeeeze.bootjpa1.web.rtimage.domain;

import java.time.LocalDateTime;

import com.cheeeeze.bootjpa1.web.remnant.domain.RemnantInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
@Table( name = "RT_IMAGE" )
public class ImageInfo {
	
	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	private String fileFullPath;
	
	@Comment( value = "사용자업로드파일명")
	private String uploadFileName;
	
	@Comment( value = "서버저장파일명")
	private String saveFileName;
	
	private Long fileSize;
	
	@Column( name = "input_date" )
	private LocalDateTime inputDate;
	
	@Column( name = "update_date" )
	private LocalDateTime updateDate;
	
	@OneToOne( mappedBy = "imageInfo" )
	private RemnantInfo remnantInfo;
	
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
	public ImageInfo( Long id, String fileFullPath, String uploadFileName, String saveFileName, Long fileSize, LocalDateTime inputDate, LocalDateTime updateDate ) {
		this.id = id;
		this.fileFullPath = fileFullPath;
		this.uploadFileName = uploadFileName;
		this.fileSize = fileSize;
		this.saveFileName = saveFileName;
		this.inputDate = inputDate;
		this.updateDate = updateDate;
	}
	
	@Override public String toString() {
		return "ImageInfo{" +
			   "id=" + id +
			   ", fileFullPath='" + fileFullPath + '\'' +
			   ", uploadFileName='" + uploadFileName + '\'' +
			   ", saveFileName='" + saveFileName + '\'' +
			   ", fileSize=" + fileSize +
			   ", inputDate=" + inputDate +
			   ", updateDate=" + updateDate +
			   '}';
	}
	
	public ImageDTO toImageDTO() {
		ImageDTO imageDTO = new ImageDTO();
		imageDTO.setId( id );
		imageDTO.setFileFullPath( fileFullPath );
		imageDTO.setSaveFileName( saveFileName );
		imageDTO.setUploadFileName( uploadFileName );
		imageDTO.setFileSize( fileSize );
		imageDTO.setInputDate( inputDate );
		imageDTO.setUpdateDate( updateDate );
		
		// remnantInfo 는 다루지 않는다(순환참조 방지)
		return imageDTO;
	}
	
	public void updateImageInfo( ImageDTO imageDto ) {
		this.fileFullPath = imageDto.getFileFullPath();
		this.saveFileName = imageDto.getSaveFileName();
		this.uploadFileName = imageDto.getUploadFileName();
		this.fileSize = imageDto.getFileSize();
	}
}
