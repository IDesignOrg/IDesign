package com.my.interrior.admin.coupon;

import org.springframework.stereotype.Service;

import com.my.interrior.client.event.coupon.CouponEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;
	
	public CouponEntity findCouponNumber(Long eventNo) {
		CouponEntity coupon = couponRepository.findBycouponNo(eventNo);
		return coupon;
	}
}
