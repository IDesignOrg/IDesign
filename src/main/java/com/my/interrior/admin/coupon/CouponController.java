package com.my.interrior.admin.coupon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.my.interrior.client.event.coupon.CouponEntity;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CouponController {

	private final CouponRepository couponRepository;
	private final CouponService couponService;
	
	@GetMapping("/admin/coupon/write")
	public String goToCoupon() {
		return "admin/coupon/couponWrite";
	}
	@PostMapping("/admin/coupon/write")
	public String couponWrite(@ModelAttribute CouponEntity couponEntity) {
		
		couponRepository.save(couponEntity);
		
		return "redirect:/admin/page/adminCouponList";
	}
	
	
}
