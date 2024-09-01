package com.my.interrior.three;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreeRepository extends JpaRepository<ThreeEntity, String> {

	// count
	int countByUserEntity_UId(String userId);

	int countByUserEntity_UIdAndTitleContaining(String userId, String title);

	//3D dashboard threeEntity 데이터들 페이징으로 받기(12개씩)
	@Query("SELECT t FROM ThreeEntity t WHERE t.UserEntity.UId = :userId AND (:title IS NULL OR t.title LIKE %:title%)")
	Page<ThreeEntity> findByUserEntity_UIdAndTitle(@Param("userId") String userId, @Param("title") String title,
			Pageable pageable);
}
