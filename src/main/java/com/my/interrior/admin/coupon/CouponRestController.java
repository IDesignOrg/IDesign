package com.my.interrior.admin.coupon;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CouponRestController {

	private final CouponService couponService;
	private final CouponRepository couponRepository;
	private final CouponMapRepository couponMap;
	private final UserRepository userRepository;

	// 나중에 couponLimit으로 사용했는지 안했는지 파악하자.(아마 결제에서)
	@PostMapping("/get/coupon/{eventNo}")
	public ResponseEntity<String> getCoupon(@PathVariable("eventNo") Long eventNo, HttpSession session) {
		CouponEntity coupon = couponService.findCouponNumber(eventNo);

		CouponMapEntity couponMapEntity = new CouponMapEntity();
		String userId = (String) session.getAttribute("UId");
		UserEntity user = userRepository.findByUId(userId);
		Optional<CouponMapEntity> existingCouponMapEntity = couponMap.findByuserEntityAndCouponEntity(user, coupon);

		if (existingCouponMapEntity.isPresent() && existingCouponMapEntity.get().isUsed()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이미 쿠폰이 발급되었습니다.");
		}
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 시 발급 가능합니다.");
		} else {
			couponMapEntity.setUserEntity(user);
			couponMapEntity.setCouponEntity(coupon);
			couponMapEntity.setUsed(true);
			couponMapEntity.setUsedDate(null);
			couponMapEntity.setAssignedDate(LocalDate.now());

			couponMap.save(couponMapEntity);

			return ResponseEntity.ok("쿠폰 발급이 완료되었습니다. 마이페이지의 내 쿠폰함에서 확인해주세요.");
		}
	}

	@GetMapping("/my/coupons")
	public ResponseEntity<List<CouponEntity>> showMyCoupons(HttpSession session) {
		String userId = (String) session.getAttribute("UId");

		UserEntity user = userRepository.findByUId(userId);

		List<CouponMapEntity> couponMapEntities = couponMap.findByuserEntity(user);
		// 여기가 마이페이지의 쿠폰인데 여기서 used인지 확인하고 썼으면 ㄴㄴ
		List<CouponEntity> validCoupons = couponMapEntities.stream()
				.filter(couponMap -> couponMap.getUsedDate() == null) // used_date가 null인 쿠폰만 가져오기
				.map(CouponMapEntity::getCouponEntity) // CouponMapEntity에서 CouponEntity를 추출
				.collect(Collectors.toList());

		return ResponseEntity.ok(validCoupons);
	}

	@Transactional
	@PatchMapping("/coupon/clear")
	public ResponseEntity<?> patchCoupon(@RequestParam(value = "couponNo", required = false) Long couponNo) {
		if (couponNo == null) {
			return ResponseEntity.ok("success");
		} else {

			CouponMapEntity coupon = couponService.getCouponBycouponNos(couponNo);

			coupon.setUsed(true);
			coupon.setUsedDate(LocalDate.now());

			System.out.println("coupon의 값은? 변경되었을 때 : " + coupon);

			couponMap.save(coupon);

			return ResponseEntity.ok("success");
		}
	}

}