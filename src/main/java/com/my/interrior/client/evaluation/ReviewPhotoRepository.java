package com.my.interrior.client.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhotoEntity, Long>{
	List<ReviewPhotoEntity> findByReview_RNo(Long rNo);
}
