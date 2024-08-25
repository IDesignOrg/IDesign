package com.my.interrior.client.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long>{
	List<ReviewCommentEntity> findByReviewEntity(ReviewEntity reviewEntity);
}