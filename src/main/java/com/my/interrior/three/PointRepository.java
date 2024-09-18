package com.my.interrior.three;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<PointEntity, Long>{
	
	List<PointEntity> findByData(DataEntity data);

}
