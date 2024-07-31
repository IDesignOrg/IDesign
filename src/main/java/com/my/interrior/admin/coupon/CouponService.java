package com.my.interrior.admin.coupon;

import java.util.List;

import org.springframework.stereotype.Service;

import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	private final CouponMapRepository couponMapRepository;
	
	public CouponEntity findCouponNumber(Long eventNo) {
		CouponEntity coupon = couponRepository.findBycouponNo(eventNo);
		return coupon;
	}
	
	public List<CouponMapEntity> getCouponNumberByUserNo(Long userNo){
		return couponMapRepository.findByuserEntity_UNo(userNo);
	}
	
	public List<CouponEntity> getCouponEntitiesBycouponNos(List<Long> couponNos){
		return couponRepository.findByCouponNoIn(couponNos);
	}
	
	public CouponMapEntity getCouponBycouponNos(Long couponNo) {
		return couponMapRepository.findBycouponEntity_couponNo(couponNo);
	}
}
