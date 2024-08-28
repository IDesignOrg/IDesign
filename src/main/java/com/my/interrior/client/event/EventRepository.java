package com.my.interrior.client.event;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
	List<EventEntity> findAll();
	
	
	Optional<EventEntity> findById(Long eventNo);
	
	Page<EventEntity> findByEventTitleContaining(String title, Pageable pageable);

    Page<EventEntity> findByCoupon_CouponNameContaining(String couponName, Pageable pageable);
	
	

}
