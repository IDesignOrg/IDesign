package com.my.interrior.client.evaluation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhotoEntity, Long>{
	// 리뷰 사진을 최적화된 방식으로 조회
    @Query("SELECT photo FROM review_photo photo WHERE photo.review.RNo = :rNo")
    List<ReviewPhotoEntity> findByReviewIdOptimized(@Param("rNo") Long rNo);
    
    // 기존 메서드 유지
    List<ReviewPhotoEntity> findByReview_RNo(Long rNo);
    
    @Transactional
    void deleteByReviewRNo(Long rNo);
}
