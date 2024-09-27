package com.my.interrior.client.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.transaction.Transactional;

public interface ReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long>{
	List<ReviewCommentEntity> findByReviewEntity(ReviewEntity reviewEntity);
	List<ReviewCommentEntity> findByReviewEntity_RNo(Long RNo);
	int countByReviewEntity(ReviewEntity reviewEntity);
	@Transactional
	void deleteByReviewEntityRNo(Long rNo);
	
}
