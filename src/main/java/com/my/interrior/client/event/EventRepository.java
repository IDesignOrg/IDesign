package com.my.interrior.client.event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
	List<EventEntity> findAll();
	
	
	Optional<EventEntity> findById(Long eventNo);
	
	

}
