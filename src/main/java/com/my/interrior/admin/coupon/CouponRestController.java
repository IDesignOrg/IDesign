package com.my.interrior.admin.coupon;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "Coupon API")
@RequestMapping("/api")
public class CouponRestController {

	private final CouponService couponService;
	private final CouponRepository couponRepository;
	private final CouponMapRepository couponMap;
	private final UserRepository userRepository;

	// 나중에 couponLimit으로 사용했는지 안했는지 파악하자.(아마 결제에서)
	@PostMapping("/coupon/{couponNo}")
	@Operation(summary = "쿠폰 발급", description = "쿠폰을 발급받았는지 확인 후 저장")
	public ResponseEntity<String> getCoupon(@PathVariable("couponNo") Long couponNo, HttpSession session) {
		// 세션에서 사용자 ID를 가져오기
		String userId = (String) session.getAttribute("UId");

		if (userId == null) {
			return ResponseEntity.status(401).body("로그인이 필요합니다."); // 인증되지 않은 사용자
		}

		try {
			// 쿠폰 번호로 쿠폰을 찾기
			CouponEntity coupon = couponService.findCouponNumber(couponNo);
			if (coupon == null) {
				return ResponseEntity.status(404).body("해당 쿠폰을 찾을 수 없습니다.");
			}

			// 쿠폰 발급 로직 처리
			UserEntity user = userRepository.findByUId(userId);
			Optional<CouponMapEntity> existingCouponMapEntity = couponMap.findByuserEntityAndCouponEntity(user, coupon);

			if (existingCouponMapEntity.isPresent() && existingCouponMapEntity.get().isUsed()) {
				return ResponseEntity.status(403).body("이미 발급된 쿠폰입니다.");
			}

			// 쿠폰 발급 처리
			CouponMapEntity couponMapEntity = new CouponMapEntity();
			couponMapEntity.setUserEntity(user);
			couponMapEntity.setCouponEntity(coupon);
			couponMapEntity.setUsed(true);
			couponMapEntity.setUsedDate(null);
			couponMapEntity.setAssignedDate(LocalDate.now());

			couponMap.save(couponMapEntity);

			return ResponseEntity.ok("쿠폰 발급이 완료되었습니다. 마이페이지에서 확인하세요.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
		}
	}

	@GetMapping("/coupons")
	@Operation(summary = "쿠폰 로드", description = "사용했는지 확인 후 로드")
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
	@PatchMapping("/coupon")
	@Operation(summary = "쿠폰 처리", description = "사용 된 쿠폰은 처리, 사용 된 날짜를 기준.")
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
