package com.my.interrior.three;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreeRepository extends JpaRepository<ThreeEntity, String> {

	int countByUserEntity_UId(String userId);
}
