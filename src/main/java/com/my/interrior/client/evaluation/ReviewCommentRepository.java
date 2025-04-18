package com.my.interrior.client.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface ReviewCommentRepository extends JpaRepository<ReviewCommentEntity, Long>{
	
	// 댓글 조회시 사용자 정보를 함께 가져오는 쿼리
    @Query("SELECT comment FROM review_comment comment JOIN FETCH comment.user WHERE comment.reviewEntity.RNo = :reviewId")
    List<ReviewCommentEntity> findByReviewIdWithUser(@Param("reviewId") Long reviewId);
    
	
	List<ReviewCommentEntity> findByReviewEntity(ReviewEntity reviewEntity);
	List<ReviewCommentEntity> findByReviewEntity_RNo(Long RNo);
	int countByReviewEntity(ReviewEntity reviewEntity);
	@Transactional
	void deleteByReviewEntityRNo(Long rNo);
	
}
