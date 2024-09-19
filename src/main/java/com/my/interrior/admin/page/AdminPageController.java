package com.my.interrior.admin.page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.my.interrior.admin.coupon.CouponMapRepository;
import com.my.interrior.admin.coupon.CouponService;
import com.my.interrior.client.csc.faq.FaqEntity;
import com.my.interrior.client.csc.inquiry.InquiryAnswerDTO;
import com.my.interrior.client.csc.inquiry.InquiryAnswerEntity;
import com.my.interrior.client.csc.inquiry.InquiryDTO;
import com.my.interrior.client.csc.inquiry.InquiryEntity;
import com.my.interrior.client.csc.inquiry.InquiryListDTO;
import com.my.interrior.client.csc.notice.NoticeEntity;
import com.my.interrior.client.csc.recover.RecoveryEntity;
import com.my.interrior.client.evaluation.ReviewCommentDTO;
import com.my.interrior.client.evaluation.ReviewCommentEntity;
import com.my.interrior.client.evaluation.ReviewEntity;
import com.my.interrior.client.evaluation.ReviewRepository;
import com.my.interrior.client.evaluation.ReviewService;
import com.my.interrior.client.event.EventEntity;
import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.ordered.OrderedEntity;
import com.my.interrior.client.ordered.OrderedRefundEntity;
import com.my.interrior.client.ordered.OrderedRefundRepository;
import com.my.interrior.client.ordered.OrderedRepository;
import com.my.interrior.client.ordered.OrderedService;
import com.my.interrior.client.pay.PayEntity;
import com.my.interrior.client.pay.PaymentAndUserService;
import com.my.interrior.client.pay.PaymentService;
import com.my.interrior.client.shop.ShopEntity;
import com.my.interrior.client.shop.ShopReviewEntity;
import com.my.interrior.client.shop.ShopReviewRepository;
import com.my.interrior.client.shop.ShopService;
import com.my.interrior.client.user.FindUserDTO;
import com.my.interrior.client.user.UserDTO;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserService;

import jakarta.persistence.EntityNotFoundException;
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
	private OrderedRepository orderedRepository;

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

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private OrderedService orderedService;

	@Autowired
	private PaymentAndUserService PaymentAndUserService;

	@Autowired
	private OrderedRefundRepository orderedRefundRepository;

	@GetMapping("/auth/adminLogin")
	public String AdminLogin() {
		return "admin/page/adminLogin";
	}

	// 어드민 로그인
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
		// 가장 높은 조회수를 가진 상점
		Optional<ShopEntity> mostViewedShop = adminPageService.getMostViewedShop();
		mostViewedShop.ifPresent(shop -> model.addAttribute("mostViewedShop", shop));
		// 가장 높은 판매량을 가진 상점
		Optional<ShopEntity> mostSelledShop = adminPageService.getMostSelledShop();
		mostSelledShop.ifPresent(shop -> model.addAttribute("mostSelledShop", shop));

		return "admin/page/adminIndex";
	}

	// 회원정보 페이지
	@GetMapping("/admin/page/adminUsers")
	public String adminUsers(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<UserWithPostAndCommentCount> usersWithCounts = adminPageService.findAllUsersWithCounts(pageable);

		model.addAttribute("usersWithCounts", usersWithCounts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", usersWithCounts.getTotalPages());
		model.addAttribute("pageSize", size);

		return "admin/page/adminUsers";
	}

	// 리뷰 리스트
	@GetMapping("/admin/adminReview")
	public String adminReview(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model) {

		Pageable pageable = PageRequest.of(page, size);
		Page<ReviewAndCommentDTO> reviewAndCounts = adminPageService.findAllReviewAndCounts(pageable);

		model.addAttribute("reviewAndCounts", reviewAndCounts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", reviewAndCounts.getTotalPages());
		model.addAttribute("pageSize", size);

		return "admin/page/adminReview";
	}
	//리뷰 댓글 
	@GetMapping("/fetchRComments")
	public ResponseEntity<List<ReviewCommentDTO>> fetchRComments(@RequestParam("reviewNo") Long reviewNo) {
		List<ReviewCommentDTO> comments = reviewService.getCommentsByReviewId(reviewNo).stream()
				.map(comment -> new ReviewCommentDTO(comment.getRCommentNo(), comment.getRComment(),
						comment.getRCommentCreated(), comment.getUser().getUName(), comment.getUser().getUPofile()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(comments);
	}
	//어드민페이지 리뷰 삭
	@DeleteMapping("/deleteReview")
    public ResponseEntity<String> deleteReview(@RequestParam("rNo") Long rNo) {
        try {
            // 리뷰 삭제 로직 호출 (댓글, 사진, GCS 파일 삭제 포함)
            adminPageService.deleteReviewById(rNo);
            return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("리뷰 삭제에 실패했습니다.");
        }
    }

	@GetMapping("/searchUsers")
	@ResponseBody
	public List<UserWithPostAndCommentCount> searchUsers(@RequestParam(name = "searchType") String searchType,
			@RequestParam(name = "searchInput") String searchInput,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(name = "orderType") String orderType) {
		// null 값 처리
		if (startDate == null)
			startDate = LocalDate.of(1900, 1, 1);
		if (endDate == null)
			endDate = LocalDate.now();

		List<UserWithPostAndCommentCount> results = adminPageService.searchUsers(searchType, searchInput, startDate,
				endDate, orderType);
		System.out.println("Search results: " + results); // 디버깅용 로그
		return results;
	}

	// 어드민 페이지 게시글 모달
	@GetMapping("/fetchPosts")
	@ResponseBody
	public ResponseEntity<?> fetchPosts(@RequestParam("userUNo") Long userUNo) {
		try {
			List<ReviewEntity> reviews = reviewRepository.findByUserUNo(userUNo);
			return ResponseEntity.ok(reviews);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching posts");
		}
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
		return "admin/page/adminNotice";
	}

	// 공지사항 삭제
	@DeleteMapping("/deleteNotice")
	@ResponseBody
	public ResponseEntity<Void> deleteNotice(@RequestParam("noticeNo") Long noticeNo) {
		adminPageService.deleteNotice(noticeNo);
		return ResponseEntity.ok().build();
	}

	// 유저 비활성화
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

	// 복구 페이지
	@GetMapping("/admin/page/adminRecovery")
	public String adminRecovery(Model model, Pageable pageable) {
		Page<RecoveryEntity> recovers = adminPageService
				.getAllRecovery(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("recovers", recovers);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", recovers.getTotalPages());
		return "admin/page/adminRecovery";
	}

	@GetMapping("/admin/page/adminRecovery/search")
	@ResponseBody
	public List<RecoveryEntity> searchRecoveryRequests(@RequestParam(name = "searchType") String searchType,
			@RequestParam(name = "searchInput", required = false) String searchInput,
			@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

		return adminPageService.searchRecoveryRequests(searchType, searchInput, startDate, endDate);
	}

	// admin 복구페이지 복구 메서드
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

	// adminUser에서 임의로 복구
	@PostMapping("/activateUser")
	@ResponseBody
	public String adminRecoveryUser(@RequestParam("userUNo") Long userUNo) {
		adminPageService.recoveryUser(userUNo);

		return "User activated successfully";
	}

	// 쿠폰 리스트
	@GetMapping("/admin/page/adminCouponList")
	public String couponManagement(Model model, Pageable pageable) {
		Page<CouponEntity> coupons = adminPageService
				.getAllCoupons(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("coupons", coupons);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", coupons.getTotalPages());
		return "admin/page/adminCouponList"; // 쿠폰 관리 페이지로 이동
	}

	// 쿠폰 모달창
	@GetMapping("/getCoupons")
	@ResponseBody
	public List<CouponEntity> getAllModalCoupons() {
		return adminPageService.getAllModalCoupons();
	}

	// 유저한테 쿠폰 발급
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

	// 유저 쿠폰 리스트
	@GetMapping("/admin/page/adminUserCoupon")
	public String userCoupon(Model model, Pageable pageable) {
		Page<CouponMapEntity> couponMaps = adminPageService
				.getAllUserCoupons(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("couponMaps", couponMaps);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", couponMaps.getTotalPages());
		return "admin/page/adminUserCoupon";
	}

	// 유저 쿠폰 삭제
	@DeleteMapping("/deleteCoupon")
	public ResponseEntity<String> deleteCoupon(@RequestParam("id") Long couponMapId) {
		try {
			adminPageService.deleteCouponById(couponMapId);
			return ResponseEntity.ok("쿠폰이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("쿠폰 삭제에 실패했습니다.");
		}
	}

	// 쿠폰 비활성화, 활성
	@PostMapping("/processCoupon")
	public ResponseEntity<String> processCoupon(@RequestParam("couponNo") Long couponNo,
			@RequestParam("state") String state) {
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

	// shop리스트와 ordered의 count
	@GetMapping("/admin/adminShopList")
	public String adminShopList(Model model, Pageable pageable) {
		Page<ShopListAndOrderedDTO> shops = adminPageService
				.getAllShopsAndCounts(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("shops", shops);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", shops.getTotalPages());
		return "admin/page/adminShopList";
	}

	@GetMapping("/adminsearch")
	public String shopList(@RequestParam(name = "shopTitle", required = false) String shopTitle,
			@RequestParam(name = "shopCategory", required = false) String shopCategory,
			@RequestParam(name = "minPrice", required = false) Integer minPrice,
			@RequestParam(name = "maxPrice", required = false) Integer maxPrice, Model model, Pageable pageable) {

		// 검색 조건을 처리하고 DTO로 변환된 결과를 가져옵니다.
		Page<ShopListAndOrderedDTO> shops = adminPageService.searchShops(shopTitle, shopCategory, minPrice, maxPrice,
				PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));

		// 결과를 모델에 추가하여 뷰에 전달합니다.
		model.addAttribute("shops", shops.getContent());
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", shops.getTotalPages());

		// 검색 조건을 다시 view에 전달하여 검색 폼에 값이 유지되도록 합니다.
		model.addAttribute("shopTitle", shopTitle);
		model.addAttribute("shopCategory", shopCategory);
		model.addAttribute("minPrice", minPrice);
		model.addAttribute("maxPrice", maxPrice);

		return "admin/page/adminShopList";
	}

	// ordered 모달창
	// orderedEntity에서 UserEntity가 @JsonIgnore 해당 어노테이션 때문에 오류로 인하여 좀 복잡하게 만듬
	@GetMapping("/fetchOrderDetails")
	@ResponseBody
	public List<Map<String, Object>> fetchOrderDetails(@RequestParam("shopNo") Long shopNo) {
		List<OrderedEntity> orders = orderedRepository.findByShopNo(shopNo);

		List<Map<String, Object>> orderDetailsList = new ArrayList<>();
		for (OrderedEntity order : orders) {
			Map<String, Object> orderDetails = new HashMap<>();
			orderDetails.put("orderedNo", order.getOrderedNo());
			orderDetails.put("orderedNumber", order.getOrderedNumber());
			orderDetails.put("orderedState", order.getOrderedState());
			orderDetails.put("shipmentState", order.getShipmentState());
			orderDetails.put("orderedDate", order.getOrderedDate());
			orderDetails.put("userName", order.getUserEntity().getUName()); // userEntity의 UName을 직접 추가
			orderDetails.put("quantity", order.getQuantity());
			orderDetailsList.add(orderDetails);
		}

		return orderDetailsList;
	}

	// 주문관리 페이지
	@GetMapping("/admin/page/adminOrdered")
	public String adminOrderedList(Model model, Pageable pageable) {
		Page<OrderedEntity> orders = adminPageService
				.getAllOrdered(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("orders", orders);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", orders.getTotalPages());
		return "admin/page/adminOrdered";
	}

	// ㅂ지활성화 할거임
	@PostMapping("/toggleShopActivation")
	public ResponseEntity<String> toggleShopActivation(@RequestParam("shopNo") Long shopNo,
			@RequestParam("isDeactivated") boolean isDeactivated) {
		try {
			adminPageService.toggleShopActivation(shopNo, isDeactivated);
			String status = isDeactivated ? "비활성화" : "활성화";
			return ResponseEntity.ok("상점이 " + status + "되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("상점 상태 변경에 실패했습니다.");
		}
	}

	// 이벤트 관리 페이지
	@GetMapping("/admin/page/adminEvent")
	public String adminEvent(Model model, Pageable pageable) {
		Page<EventEntity> events = adminPageService.getAllEvent(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("events", events);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", events.getTotalPages());
		return "admin/page/adminEvent";
	}

	@GetMapping("/admin/page/adminEventSearch")
	public String searchEvents(@RequestParam("type") String type, @RequestParam("keyword") String keyword,
			Pageable pageable, Model model) {

		Page<EventEntity> events;
		if ("title".equals(type)) {
			events = adminPageService.searchEventsByTitle(keyword, pageable);
		} else if ("couponName".equals(type)) {
			events = adminPageService.searchEventsByCouponName(keyword, pageable);
		} else {
			events = adminPageService.getAllEvent(pageable); // 기본으로 전체 이벤트 가져오기
		}

		model.addAttribute("events", events);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", events.getTotalPages());
		return "admin/page/adminEvent";
	}

	// 환불
	@PostMapping("/refund/paymentToAdmin")
	public ResponseEntity<?> refundPayment(@RequestParam("merchantUId") String merchantUId,
			@RequestParam("refundReason") String refundReason, HttpSession session) throws Exception {
		String userid = (String) session.getAttribute("UId");
		String token = paymentService.getAccessToken();
		System.out.println("멀천튜유아이딩 : "+merchantUId);
		System.out.println("뤼펀드우느랒 : " + refundReason);
		paymentService.refundRequest(token, merchantUId);
		

		// 여기에 주문 내역에서 삭제해야 됨.
		// ordered랑 payment랑 payment_user_mapping 테이블 전부인데 payment_user_mapping부터 지워야 됨.
		// shipment는 나중에 시간나면 추가해줘야 됨. 지금 shipment 지울만한 속성이 없음.
		// 오더 상태 변경
		orderedService.updateOrderedState(merchantUId, refundReason, userid);

		PayEntity pay = paymentService.findPayEntity(merchantUId);
		Long payNo = pay.getPayNo();
		// payment_user_mapping 제거
		PaymentAndUserService.deleteByPayNo(payNo);
		// payment 제거
		paymentService.deleteByMerchantUId(merchantUId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/refund/reason")
	public ResponseEntity<?> getRefundReason(@RequestParam("merchantUId") String merchantUId) {
		OrderedRefundEntity refundEntity = orderedRefundRepository.findByOrderedEntity_MerchantUId(merchantUId)
				.orElseThrow(() -> new EntityNotFoundException("No refund found for merchantUId: " + merchantUId));

		return ResponseEntity
				.ok(Map.of("refundReason", refundEntity.getRefundReason(), "refundUser", refundEntity.getRefundUser() // 추가된
																														// 필드
				));
	}

	@PostMapping("/admin/updateShipmentState")
	public ResponseEntity<?> updateShipmentState(@RequestParam("orderedNo") Long orderedNo,
			@RequestParam("shipmentState") String shipmentState) {

		try {
			// 주문 번호를 기반으로 주문을 조회하고 상태 업데이트
			adminPageService.updateShipmentState(orderedNo, shipmentState);

			// 성공적으로 업데이트된 경우 HTTP 200 응답을 반환
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			// 오류가 발생한 경우 HTTP 500 응답을 반환
			return ResponseEntity.status(500).body("주문 상태 업데이트 중 오류가 발생했습니다.");
		}
	}

	// 자주 묻는 질문 리스
	@GetMapping("/admin/adminFAQ")
	public String getFaqList(Model model, Pageable pageable) {
		Page<FaqEntity> faqs = adminPageService.getAllFaq(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("faqs", faqs);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", faqs.getTotalPages());
		return "admin/page/adminFAQ";
	}

	// 자주 묻는 질문 수정 모달창
	@GetMapping("/admin/faq/{faqNo}")
	@ResponseBody
	public ResponseEntity<FaqEntity> getFaq(@PathVariable("faqNo") Long faqNo) {
		System.out.println("Fetching FAQ with ID: " + faqNo);
		FaqEntity faq = adminPageService.getFaqById(faqNo);
		if (faq != null) {
			return ResponseEntity.ok(faq);
		} else {
			System.out.println("FAQ not found with ID: " + faqNo);
			return ResponseEntity.notFound().build();
		}
	}

	// 자주 묻는 질문 수정
	@PutMapping("/admin/faq/{faqNo}")
	@ResponseBody
	public ResponseEntity<String> updateFaq(@PathVariable("faqNo") Long faqNo, @RequestBody FaqEntity faqData) {
		boolean updated = adminPageService.updateFaq(faqNo, faqData);
		if (updated) {
			return ResponseEntity.ok("FAQ가 성공적으로 수정되었습니다.");
		} else {
			return ResponseEntity.badRequest().body("FAQ 수정에 실패했습니다.");
		}
	}

	@DeleteMapping("/admin/faq/{faqNo}")
	@ResponseBody
	public ResponseEntity<String> deleteFaq(@PathVariable("faqNo") Long faqNo) {
		boolean deleted = adminPageService.deleteFaq(faqNo);
		if (deleted) {
			return ResponseEntity.ok("FAQ가 성공적으로 삭제되었습니다.");
		} else {
			return ResponseEntity.badRequest().body("FAQ 삭제에 실패했습니다.");
		}
	}
	//문의사항
	@GetMapping("/admin/inquiry")
    public String getInquiryList(Model model, Pageable pageable) {
        // 서비스에서 모든 문의를 조회하여 DTO로 변환
        Page<InquiryListDTO> inquiries = adminPageService.getAllInquiries(pageable);
        
        // 모델에 데이터 추가
        model.addAttribute("inquirys", inquiries.getContent());  // 페이지 내용만 전달
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", inquiries.getTotalPages());

        return "admin/page/adminInquiry";  // Thymeleaf 템플릿 경로
    }
	
	//문의사항 모달창(답변보기)
	@GetMapping("/getInquiryDetails")
	@ResponseBody
	public InquiryDTO getInquiryDetails(@RequestParam("inqNo") Long inqNo) {
		InquiryEntity inquiryEntity = adminPageService.getInquiryById(inqNo);
		//문의테이블에서 값을 dto에 입력
		InquiryDTO inquiryDTO = new InquiryDTO();
		inquiryDTO.setInqNo(inquiryEntity.getInqNo());
		inquiryDTO.setInqTitle(inquiryEntity.getInqTitle());
		inquiryDTO.setInqRegisteredDate(inquiryEntity.getInqRegisteredDate());
		inquiryDTO.setInqCategory(inquiryEntity.getInqCategory());
		inquiryDTO.setInqContent(inquiryEntity.getInqContent());
		//findUserDTO에 원하는 값을 넣고 inquiryDTO의 User에 값을 넣음
		FindUserDTO findUserDTO = new FindUserDTO();
		findUserDTO.setUName(inquiryEntity.getUserEntity().getUName());
		findUserDTO.setUNo(inquiryEntity.getUserEntity().getUNo());
		findUserDTO.setUPofile(inquiryEntity.getUserEntity().getUPofile());
		inquiryDTO.setUser(findUserDTO);
		System.out.println("InquiryDTO: " + inquiryDTO);
		//답변하기
		InquiryAnswerEntity answerEntity = adminPageService.getInquiryAnswerById(inqNo);
        if (answerEntity != null) {
            InquiryAnswerDTO answerDTO = new InquiryAnswerDTO();
            answerDTO.setAnsNo(answerEntity.getAnsNo());
            answerDTO.setAnsContent(answerEntity.getAnsContent());
            answerDTO.setAnsRegisteredDate(answerEntity.getAnsRegisteredDate());

            // UserEntity -> UserDTO for the answer
            FindUserDTO answerUserDTO = new FindUserDTO();
            answerUserDTO.setUName(answerEntity.getUserEntity().getUName());
            answerUserDTO.setUPofile(answerEntity.getUserEntity().getUPofile());
            answerUserDTO.setUNo(answerEntity.getUserEntity().getUNo());
            answerDTO.setUser(answerUserDTO);

            inquiryDTO.setAnswer(answerDTO); // 단일 답변 설정
        }else {
            inquiryDTO.setAnswer(null); // 답변이 없으면 null 설정
        }

        return inquiryDTO;
	}
	// 문의 상세 정보 조회 (답변 작성 페이지 로드용)
	//문의사항 모달창(답변보내기)
	@PostMapping("/submitAnswer")
    @ResponseBody
    public ResponseEntity<String> submitAnswer(@RequestParam("inqNo") Long inqNo, @RequestParam("answerContent") String answerContent) {
        try {
            adminPageService.saveInquiryAnswer(inqNo, answerContent);
            return ResponseEntity.ok("답변이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("답변 저장에 실패했습니다.");
        }
    }

}
