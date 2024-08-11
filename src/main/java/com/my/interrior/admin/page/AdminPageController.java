package com.my.interrior.admin.page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.my.interrior.admin.coupon.CouponMapRepository;
import com.my.interrior.admin.coupon.CouponRepository;
import com.my.interrior.admin.coupon.CouponService;
import com.my.interrior.client.csc.notice.NoticeEntity;
import com.my.interrior.client.csc.recover.RecoveryEntity;
import com.my.interrior.client.evaluation.ReviewEntity;
import com.my.interrior.client.evaluation.ReviewRepository;
import com.my.interrior.client.evaluation.ReviewService;
import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.shop.ShopReviewEntity;
import com.my.interrior.client.shop.ShopReviewRepository;
import com.my.interrior.client.shop.ShopService;
import com.my.interrior.client.user.UserDTO;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminPageController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private UserService userService;

	@Autowired
	private AdminPageService adminPageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ShopReviewRepository shopReviewRepository;

	@Autowired
	private ShopService shopService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponMapRepository couponMap;

	@GetMapping("/auth/adminLogin")
	public String AdminLogin() {
		return "admin/page/adminLogin";
	}

	@PostMapping("/auth/adminlogin")
	public String adminjoin(@ModelAttribute UserDTO userDTO, HttpSession session, Model model) throws Exception {
		try {
			if (!"admin".equals(userDTO.getUId())) {
				model.addAttribute("loginError", "관리자 아이디를 입력하세요.");
				return "admin/page/adminLogin";
			}

			if (userDTO.getUPw() == null || userDTO.getUPw().isEmpty()) {
				model.addAttribute("loginError", "비밀번호를 입력하세요.");
				return "admin/page/adminLogin";
			}
			String UId = userDTO.getUId();
			String UPw = userDTO.getUPw();
			UserEntity user = userService.checkLogin(UId);
			System.out.println("user" + user);
			if (user != null && passwordEncoder.matches(UPw, user.getUPw())) {
				session.setAttribute("UId", user.getUId());
				return "redirect:/admin/page/adminIndex";
			} else {
				model.addAttribute("loginError", "입력하신 정보를 확인하세요.");
				return "admin/page/adminLogin";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	// 인덱스 페이지
	@GetMapping("/admin/page/adminIndex")
	public String adminIndex(HttpSession session, Model model) {
		String adminId = (String) session.getAttribute("UID");
		// 유저 수 체크
		Long userCount = adminPageService.getUserCount();
		System.out.println("유저 수는 : " + userCount);
		model.addAttribute("userCount", userCount);
		// 상점 후기 체크
		Long shopCount = adminPageService.getShopCount();
		System.out.println("쇼핑몰 후기 수는  : " + shopCount);
		model.addAttribute("shopCount", shopCount);
		// 리뷰 수
		Long reviewCount = adminPageService.getReviewCount();
		System.out.println("리뷰의 수 : " + reviewCount);
		model.addAttribute("reviewCount", reviewCount);

		return "/admin/page/adminIndex";
	}

	// 회원정보 페이지
	@GetMapping("/admin/page/adminUsers")
	public String adminUsers(Model model) {
		// findAllUsersWithCounts() 메서드는 UserWithPostAndCommentCount 리스트를 반환함
		List<UserWithPostAndCommentCount> usersWithCounts = adminPageService.findAllUsersWithCounts();
		model.addAttribute("usersWithCounts", usersWithCounts);

		return "/admin/page/adminUsers"; // 뷰의 이름을 반환
	}

	// 어드민 페이지 게시글 모달
	@GetMapping("/fetchPosts")
	@ResponseBody
	public List<ReviewEntity> fetchPosts(@RequestParam("userUNo") Long userUNo) {
		// ReviewRepository에서 UNo 기준으로 데이터 가져오기
		return reviewRepository.findByUserUNo(userUNo);
	}

	// 어드민 페이지 게시글 모달 삭제
	@DeleteMapping("/deletePost")
	public ResponseEntity<Void> deletePost(@RequestParam("rNo") Long rNo) {
		reviewService.deleteReview(rNo);
		return ResponseEntity.ok().build();
	}

	// 어드민 페이지 댓글 모달
	@GetMapping("/fetchComments")
	@ResponseBody
	public List<ShopReviewEntity> fetchComments(@RequestParam("userUNo") Long userUNo) {
		return shopReviewRepository.findByUserUNo(userUNo);
	}

	// 어드민 페이지 댓글 모달 삭제
	@DeleteMapping("/deleteComment")
	public ResponseEntity<Void> deleteComment(@RequestParam("shopReviewNo") Long shopReviewNo) {
		shopService.deleteShopReview(shopReviewNo);
		return ResponseEntity.ok().build();
	}

	// 공지사항
	@GetMapping("/admin/page/adminNotice")
	public String adminNoticeList(Model model, Pageable pageable) {
		Page<NoticeEntity> notices = adminPageService.getAllNotice(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("notices", notices);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", notices.getTotalPages());
		return "/admin/page/adminNotice";
	}

	// 공지사항 삭제
	@DeleteMapping("/deleteNotice")
	@ResponseBody
	public ResponseEntity<Void> deleteNotice(@RequestParam("noticeNo") Long noticeNo) {
		adminPageService.deleteNotice(noticeNo);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/deactivateUser")
	public ResponseEntity<String> deactivateUser(@RequestParam("userUNo") Long userUNo) {
		try {
			// 사용자 비활성화 서비스 호출
			boolean isDeactivated = userService.deactivateUser(userUNo);

			if (isDeactivated) {
				return ResponseEntity.ok("해당 유저는 비활성화 되었습니다.");
			} else {
				return ResponseEntity.status(400).body("유저 비활성화에 실패했습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("서버 오류로 인해 유저 비활성화에 실패했습니다.");
		}
	}

	@GetMapping("/admin/page/adminRecovery")
	public String adminRecovery(Model model, Pageable pageable) {
		Page<RecoveryEntity> recovers = adminPageService
				.getAllRecovery(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("recovers", recovers);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", recovers.getTotalPages());
		return "/admin/page/adminRecovery";
	}

	@PostMapping("/processRecovery")
	public String processRecovery(@RequestParam("recoverNo") Long recoverNo, Model model) {
		try {
			// 복구 요청 처리
			adminPageService.processRecovery(recoverNo);

			// 처리 후 성공 메시지 추가
			model.addAttribute("message", "복구가 성공적으로 처리되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "복구 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");
		}

		// 처리 후 복구 목록 페이지로 리다이렉트
		return "redirect:/admin/page/adminRecovery";
	}

	@GetMapping("/admin/page/adminCouponList")
	public String couponManagement(Model model) {
		List<CouponEntity> coupons = adminPageService.getAllCoupons();
		model.addAttribute("coupons", coupons);
		return "admin/page/adminCouponList"; // 쿠폰 관리 페이지로 이동
	}

	// 쿠폰 모달창
	@GetMapping("/getCoupons")
	@ResponseBody
	public List<CouponEntity> getCoupons() {
		return adminPageService.getAllCoupons();
	}
	//유저한테 쿠폰 발급
	@PostMapping("/issueCouponToUser")
	public ResponseEntity<String> issueCouponToUser(@RequestBody CouponDTO request) {
		UserEntity user = userService.findById(request.getUserNo());
		CouponEntity coupon = couponService.findCouponNumber(request.getCouponNo());
		CouponMapEntity couponMapEntity = new CouponMapEntity();
		Optional<CouponMapEntity> existingCouponMapEntity = couponMap.findByuserEntityAndCouponEntity(user, coupon);
		if (existingCouponMapEntity.isPresent() && existingCouponMapEntity.get().isUsed()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이미 쿠폰이 발급되었습니다.");
		}

		couponMapEntity.setUserEntity(user);
		couponMapEntity.setCouponEntity(coupon);
		couponMapEntity.setUsed(true);
		couponMapEntity.setUsedDate(null);
		couponMapEntity.setAssignedDate(LocalDate.now());

		couponMap.save(couponMapEntity);

		return ResponseEntity.ok("쿠폰 발급이 완료되었습니다. 마이페이지의 내 쿠폰함에서 확인해주세요.");
	}
	//유저 쿠폰 리스트
	@GetMapping("/admin/page/adminUserCoupon")
	public String userCoupon(Model model) {
		List<CouponMapEntity> couponMaps = adminPageService.getAllUserCoupons();
		model.addAttribute("couponMaps", couponMaps);
		return "/admin/page/adminUserCoupon";
	}
	//유저 쿠폰 삭제
	@DeleteMapping("/deleteCoupon")
    public ResponseEntity<String> deleteCoupon(@RequestParam("id") Long couponMapId) {
        try {
            adminPageService.deleteCouponById(couponMapId);
            return ResponseEntity.ok("쿠폰이 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("쿠폰 삭제에 실패했습니다.");
        }
    }
	//쿠폰 비활성화, 활성
	 @PostMapping("/processCoupon")
	    public ResponseEntity<String> processCoupon(@RequestParam("couponNo") Long couponNo, @RequestParam("state") String state) {
	        try {
	            CouponEntity coupon = couponService.findCouponById(couponNo);
	            
	            // 현재 날짜와 쿠폰 만료일 비교
	            if (state.equalsIgnoreCase("ACTIVE") && coupon.getCouponEndAt().isBefore(LocalDate.now())) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("쿠폰의 유효 기간이 만료되어 활성화할 수 없습니다.");
	            }

	            adminPageService.updateCouponState(couponNo, state);
	            return ResponseEntity.ok("쿠폰 상태가 업데이트되었습니다.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("쿠폰 상태 업데이트에 실패했습니다.");
	        }
	    }
}
