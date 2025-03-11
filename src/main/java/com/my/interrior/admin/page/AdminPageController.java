package com.my.interrior.admin.page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.interrior.admin.page.adminDTO.ReviewAndCommentDTO;
import com.my.interrior.admin.page.adminDTO.ShopListAndOrderedDTO;
import com.my.interrior.client.csc.faq.FaqEntity;
import com.my.interrior.client.csc.inquiry.InquiryListDTO;
import com.my.interrior.client.csc.notice.NoticeEntity;
import com.my.interrior.client.csc.recover.RecoveryEntity;
import com.my.interrior.client.event.EventEntity;
import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.ordered.OrderedEntity;
import com.my.interrior.client.shop.ShopEntity;
import com.my.interrior.client.user.UserDTO;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
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
		//어드민 유저 정보
		String userId = (String) session.getAttribute("UId");
		System.out.println("user ID : " + userId);
		//세션값에 아이디값이 없을경우 다시 어드민 로그인 페이지로
		if (userId == null) {
			return "redirect:admin/page/adminLogin";
		}
		//세션값의 UId를 통해서해당 값을 찾음
		UserEntity user = userService.findByUId(userId);
		//모델아 담아서 html에 출력
		model.addAttribute("user", user);
		// 유저 수 체크
		Long userCount = adminPageService.getUserCount();
		model.addAttribute("userCount", userCount);
		// 상점 후기 체크
		Long shopCount = adminPageService.getShopCount();
		model.addAttribute("shopCount", shopCount);
		// 리뷰 수
		Long reviewCount = adminPageService.getReviewCount();
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

	// 리뷰 리스트api
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

	@GetMapping("/searchUsers")
	@ResponseBody
	@Operation(hidden = true)
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

	// 공지사항
	@GetMapping("/admin/page/adminNotice")
	public String adminNoticeList(Model model, Pageable pageable) {
		Page<NoticeEntity> notices = adminPageService.getAllNotice(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("notices", notices);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", notices.getTotalPages());
		return "admin/page/adminNotice";
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
	@Operation(hidden = true)
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

	// shop리스트와 ordered의 count
	@GetMapping("/admin/page/adminShopList")
	public String adminShopList(Model model, Pageable pageable) {
		Page<ShopListAndOrderedDTO> shops = adminPageService
				.getAllShopsAndCounts(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("shops", shops);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", shops.getTotalPages());
		return "admin/page/adminShopList";
	}
	
	@GetMapping("/adminsearch")
	@Operation(hidden = true)
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

	// 자주 묻는 질문 리스
	@GetMapping("/admin/adminFAQ")
	public String getFaqList(Model model, Pageable pageable) {
		Page<FaqEntity> faqs = adminPageService.getAllFaq(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
		model.addAttribute("faqs", faqs);
		model.addAttribute("currentPage", pageable.getPageNumber());
		model.addAttribute("totalPages", faqs.getTotalPages());
		return "admin/page/adminFAQ";
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


}
