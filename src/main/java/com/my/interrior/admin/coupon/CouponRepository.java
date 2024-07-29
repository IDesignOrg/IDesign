package com.my.interrior.admin.coupon;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.interrior.client.event.coupon.CouponEntity;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long>{
	CouponEntity findBycouponNo(Long eventNo);
	
	List<CouponEntity> findByCouponNoIn(List<Long> couponNos);
}
