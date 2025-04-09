package com.my.interrior.client.evaluation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.my.interrior.client.user.UserEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

	// 리뷰 조회시 사용자 정보를 함께 가져오는 쿼리
	@Query("SELECT r FROM review r JOIN FETCH r.user WHERE r.RNo = :rNo")
	Optional<ReviewEntity> findByIdWithUser(@Param("rNo") Long rNo);
    
    
    int countByUser(UserEntity user);
    List<ReviewEntity> findByUserUNo(Long userUNo);
    ReviewEntity findByRNo(Long rNo);
}
