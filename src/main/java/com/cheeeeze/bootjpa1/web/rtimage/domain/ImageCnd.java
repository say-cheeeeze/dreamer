package com.cheeeeze.bootjpa1.web.rtimage.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageCnd {
	
	private Long id;
	private String fileFullPath;
	private String uploadFileName;
	private String saveFileName;
	private Long fileSize;
	private LocalDateTime inputDate;
	private LocalDateTime updateDate;

}
