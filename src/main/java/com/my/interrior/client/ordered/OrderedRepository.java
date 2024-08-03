package com.my.interrior.client.ordered;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedRepository extends JpaRepository<OrderedEntity, Long>{

	List<OrderedEntity> findByUserEntity_UId(String UId);
}
