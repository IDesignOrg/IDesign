package com.my.interrior.three;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository extends JpaRepository<DataEntity, Long>{

	List<DataEntity> findByThreeEntity(ThreeEntity threeEntity);
	List<DataEntity> findByThreeEntity_ProjectIdIn(List<String> projectIds);
}
