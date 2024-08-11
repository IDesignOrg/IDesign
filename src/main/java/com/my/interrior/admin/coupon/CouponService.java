package com.my.interrior.admin.coupon;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public CouponEntity findCouponById(Long couponNo) {
        return couponRepository.findById(couponNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰을 찾을 수 없습니다: " + couponNo));
    }
	
	//만료 쿠폰
	@Scheduled(cron = "0 0 0 * * *") // 매일 자정(00시)
	@Transactional
	public void expirationcoupon() {
		//오늘 날짜 
		LocalDate today = LocalDate.now();
		//쿠폰 만료날(couponEndAt) == today and couponState == true 를 매일 자정 마다 찾는다.
		List<CouponEntity> expiredCoupons = couponRepository.findByCouponEndAtBeforeAndCouponState(today, "true");
		
		for (CouponEntity coupon : expiredCoupons) {
            coupon.setCouponState("false");
        }
		
		couponRepository.saveAll(expiredCoupons);
	}
}
