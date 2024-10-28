package com.cheeeeze.bootjpa1.web.rtimage.service;

import com.cheeeeze.bootjpa1.web.rtimage.domain.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageInfoRepository extends JpaRepository<ImageInfo, Long> {
}
