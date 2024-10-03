package com.my.interrior.admin.page;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.interrior.admin.coupon.CouponMapRepository;
import com.my.interrior.admin.coupon.CouponService;
import com.my.interrior.admin.page.adminDTO.CouponDTO;
import com.my.interrior.admin.page.adminDTO.FaqDTO;
import com.my.interrior.admin.page.adminDTO.UserShopCommentDTO;
import com.my.interrior.client.csc.faq.FaqEntity;
import com.my.interrior.client.csc.inquiry.InquiryAnswerDTO;
import com.my.interrior.client.csc.inquiry.InquiryAnswerEntity;
import com.my.interrior.client.csc.inquiry.InquiryDTO;
import com.my.interrior.client.csc.inquiry.InquiryEntity;
import com.my.interrior.client.evaluation.ReviewEntity;
import com.my.interrior.client.evaluation.ReviewService;
import com.my.interrior.client.evaluation.DTO.ReviewCommentDTO;
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
import com.my.interrior.client.shop.ShopReviewRepository;
import com.my.interrior.client.shop.ShopService;
import com.my.interrior.client.shop.shopDTO.ShopDTO;
import com.my.interrior.client.user.FindUserDTO;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserService;
import com.my.interrior.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Admin", description = "Admin API")
@RestController
@Slf4j
@RequestMapping("/admin/api")
public class AdminPageRestController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private AdminPageService adminPageService;

	@Autowired
	private UserService userService;

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponMapRepository couponMap;

	@Autowired
	private ShopService shopService;

	@Autowired
	private ShopReviewRepository shopReviewRepository;

	@Autowired
	private OrderedService orderedService;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentAndUserService PaymentAndUserService;

	@Autowired
	private OrderedRefundRepository orderedRefundRepository;

	@Autowired
	private OrderedRepository orderedRepository;

	private static final int PAGE_SIZE = 10;

//-----------------------어드민 리뷰 페이지----------------------
	// admin페이지 리뷰 댓글 보기
	@GetMapping("/getRComments")
	@Operation(summary = "리뷰페이지 댓글", description = "리뷰 페이지 댓글 본다.")
	public ResponseEntity<CommonResponse<List<ReviewCommentDTO>>> fetchRComments(
			@Parameter(name = "reviewNo", description = "리뷰 번호를 받는 dto") @RequestParam("reviewNo") Long reviewNo) {
		List<ReviewCommentDTO> comments = reviewService.getCommentsByReviewId(reviewNo).stream()
				.map(comment -> new ReviewCommentDTO(comment.getRCommentNo(), comment.getRComment(),
						comment.getRCommentCreated(), comment.getUser().getUName(),comment.getUser().getUId(), comment.getUser().getUPofile()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(CommonResponse.success(comments));
	}

	// 리뷰페이지에서 리뷰 삭제
	@DeleteMapping("/Review")
	@Operation(summary = "리뷰 삭제", description = "어드민 페이지에서 리뷰를 삭제 시킨다.")
	public ResponseEntity<CommonResponse<String>> deleteReview(@RequestParam("rNo") Long rNo) {
		if (rNo == null || rNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 리뷰 번호입니다.");
		}

		if (!adminPageService.existsReviewById(rNo)) {
			throw new NoSuchElementException("해당 리뷰가 존재하지 않습니다.");
		}

		// 리뷰 삭제 로직 호출 (댓글, 사진, GCS 파일 삭제 포함)
		adminPageService.deleteReviewById(rNo);

		return ResponseEntity.ok(CommonResponse.success("리뷰가 성공적으로 삭제되었습니다."));
	}

//--------------------어드민유저 페이지-------------------
	// 어드민 유저 페이지 게시글 모달
	@GetMapping("/user/Posts")
	@ResponseBody
	@Operation(summary = "게시글 모달", description = "특정 회원의 게시글을 볼 수 있다(리뷰).")
	public ResponseEntity<CommonResponse<List<ReviewEntity>>> fetchPosts(@RequestParam("userUNo") Long userUNo) {
		List<ReviewEntity> reviews = adminPageService.getPostsByUser(userUNo);
		return ResponseEntity.ok(CommonResponse.success(reviews));
	}

	// 어드민 유저 페이지 게시글 모달 삭제
	@DeleteMapping("/user/Post")
	@Operation(summary = "게시글 삭제", description = "특정 회원의 게시글을 삭제 시킬 수 있다.")
	public ResponseEntity<CommonResponse<String>> deletePost(@RequestParam("rNo") Long rNo) {
		reviewService.deleteReview(rNo);
		return ResponseEntity.ok(CommonResponse.success("게시글이 성공적으로 삭제되었습니다."));
	}

	// 유저 비활성화
	@PatchMapping("/deactivateUser")
	@Operation(summary = "유저 비활성화", description = "유저를 비활성화 시켜서 로그인이 가능하지 않게 한다.")
	public ResponseEntity<CommonResponse<String>> deactivateUser(@RequestParam("userUNo") Long userUNo) {
		// 유효하지 않은 사용자 번호가 들어오면 IllegalArgumentException 발생
		if (userUNo == null || userUNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 사용자 번호입니다.");
		}

		// 사용자 비활성화 서비스 호출
		boolean isDeactivated = userService.deactivateUser(userUNo);

		// 비활성화 성공 여부에 따른 응답 반환
		if (isDeactivated) {
			return ResponseEntity.ok(CommonResponse.success("해당 유저는 비활성화 되었습니다."));
		} else {
			throw new IllegalArgumentException("유저 비활성화에 실패했습니다.");
		}
	}

	// 유저 활성화
	@PatchMapping("/activateUser")
	@Operation(summary = "유저 활성화", description = "비활성화된 유저를 활성화(로그인가능)한다.")
	public ResponseEntity<CommonResponse<String>> adminRecoveryUser(@RequestParam("userUNo") Long userUNo) {
		if (userUNo == null || userUNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 사용자 번호입니다.");
		}

		try {
			adminPageService.recoveryUser(userUNo);
			return ResponseEntity.ok(CommonResponse.success("해당 유저는 성공적으로 활성화되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 유저를 찾을 수 없습니다.");
		}
	}

	// 유저한테 쿠폰 발급
	@PostMapping("/get/CouponToUser")
	@Operation(summary = "유저 쿠폰 발급", description = "특정 유저에게 쿠폰을 발급한다.")
	public ResponseEntity<CommonResponse<String>> issueCouponToUser(@RequestBody CouponDTO request) {
		UserEntity user = userService.findById(request.getUserNo());
		CouponEntity coupon = couponService.findCouponNumber(request.getCouponNo());

		Optional<CouponMapEntity> existingCouponMapEntity = couponMap.findByuserEntityAndCouponEntity(user, coupon);
		if (existingCouponMapEntity.isPresent() && existingCouponMapEntity.get().isUsed()) {
			throw new IllegalArgumentException("이미 쿠폰이 발급되었습니다.");
		}

		CouponMapEntity couponMapEntity = new CouponMapEntity();
		couponMapEntity.setUserEntity(user);
		couponMapEntity.setCouponEntity(coupon);
		couponMapEntity.setUsed(true);
		couponMapEntity.setUsedDate(null);
		couponMapEntity.setAssignedDate(LocalDate.now());

		couponMap.save(couponMapEntity);

		return ResponseEntity.ok(CommonResponse.success("쿠폰 발급이 완료되었습니다. 마이페이지의 내 쿠폰함에서 확인해주세요."));
	}

	// 어드민 페이지 댓글 모달 삭제
	@DeleteMapping("/user/ShopComment")
	@Operation(summary = "유저 댓글 삭제", description = "특정 유저의 댓글을 삭제한다.")
	public ResponseEntity<CommonResponse<String>> deleteComment(@RequestParam("shopReviewNo") Long shopReviewNo) {
		if (shopReviewNo == null || shopReviewNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 댓글 번호입니다.");
		}

		try {
			shopService.deleteShopReview(shopReviewNo);
			return ResponseEntity.ok(CommonResponse.success("댓글이 성공적으로 삭제되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 댓글을 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("댓글 삭제 중 오류가 발생했습니다.");
		}
	}

	// 어드민 페이지 유저 댓글 (무한재귀형산으로 인한 DTO 생성)
	@GetMapping("/user/Comments")
	@ResponseBody
	@Operation(summary = "유저 상품 리뷰", description = "특정 유저의 상품 리뷰를 확인한다.")
	public ResponseEntity<CommonResponse<List<UserShopCommentDTO>>> fetchComments(
			@RequestParam("userUNo") Long userUNo) {
		if (userUNo == null || userUNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 사용자 번호입니다.");
		}

		try {
			List<UserShopCommentDTO> comments = shopReviewRepository.findByUserUNo(userUNo).stream()
					.map(review -> new UserShopCommentDTO(review.getShopReviewNo(), review.getShopReviewContent(),
							review.getShopReviewCreated(), review.getShopReviewStarRating()))
					.collect(Collectors.toList());

			if (comments.isEmpty()) {
				throw new NoSuchElementException("해당 사용자에게 작성된 댓글이 없습니다.");
			}

			return ResponseEntity.ok(CommonResponse.success(comments));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
		}
	}
	
	@GetMapping("/user/showCoupons")
	@Operation(summary = "유저 쿠폰 모달창", description = "특정 유저에게 admin이 쿠폰을 발급해 줄 수 있는 모달창을 띄운다.")
	public ResponseEntity<CommonResponse<List<CouponEntity>>> getAllModalCoupons() {
	    try {
	        List<CouponEntity> coupons = adminPageService.getAllModalCoupons();
	        return ResponseEntity.ok(CommonResponse.success(coupons));
	    } catch (Exception e) {
	        throw new IllegalArgumentException("쿠폰을 불러오는 중 오류가 발생했습니다.");
	    }
	}


	// -----------------------------------공지사항------------------------------------
	@DeleteMapping("/Notice")
	@ResponseBody
	@Operation(summary = "공지사항 삭제", description = "선택한 공지사항을 삭제한다.")
	public ResponseEntity<CommonResponse<String>> deleteNotice(@RequestParam("noticeNo") Long noticeNo) {
		if (noticeNo == null || noticeNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 공지사항 번호입니다.");
		}

		try {
			adminPageService.deleteNotice(noticeNo);
			return ResponseEntity.ok(CommonResponse.success("공지사항이 성공적으로 삭제되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 공지사항을 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("공지사항 삭제 중 오류가 발생했습니다.");
		}
	}

	// ---------------------------------쿠폰-----------------------------------------
	@DeleteMapping("/user/Coupon")
	@Operation(summary = "유저 쿠폰 삭제", description = "유저 쿠폰 페이지에서 선택한 유저의 쿠폰을 삭제한다.")
	public ResponseEntity<CommonResponse<String>> deleteCoupon(@RequestParam("id") Long couponMapId) {
		if (couponMapId == null || couponMapId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 쿠폰 ID입니다.");
		}

		try {
			adminPageService.deleteCouponById(couponMapId);
			return ResponseEntity.ok(CommonResponse.success("쿠폰이 성공적으로 삭제되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 쿠폰을 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("쿠폰 삭제 중 오류가 발생했습니다.");
		}
	}

	@PatchMapping("/processCoupon")
	@Operation(summary = "쿠폰 비활성화, 활성화", description = "해당 쿠폰을 비활성화, 활성화 시킨다.")
	public ResponseEntity<CommonResponse<String>> processCoupon(@RequestParam("couponNo") Long couponNo,
			@RequestParam("state") String state) {
		if (couponNo == null || couponNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 쿠폰 번호입니다.");
		}

		try {
			CouponEntity coupon = couponService.findCouponById(couponNo);

			// 현재 날짜와 쿠폰 만료일 비교
			if (state.equalsIgnoreCase("true") && coupon.getCouponEndAt().isBefore(LocalDate.now())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(CommonResponse.failure("쿠폰의 유효 기간이 만료되어 활성화할 수 없습니다."));
			}

			adminPageService.updateCouponState(couponNo, state);
			return ResponseEntity.ok(CommonResponse.success("쿠폰 상태가 성공적으로 업데이트되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 쿠폰을 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("쿠폰 상태 업데이트 중 오류가 발생했습니다.");
		}
	}

//------------------------------상점---------------------------
	@PatchMapping("/processShop")
	@Operation(summary = "상점 비활성화, 활성화", description = "상점을 비활성화, 활성화 한다. 비활성화시 검색 불가.")
	public ResponseEntity<CommonResponse<String>> toggleShopActivation(@RequestParam("shopNo") Long shopNo,
			@RequestParam("isDeactivated") boolean isDeactivated) {
		if (shopNo == null || shopNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 상점 번호입니다.");
		}

		try {
			adminPageService.toggleShopActivation(shopNo, isDeactivated);
			String status = isDeactivated ? "비활성화" : "활성화";
			return ResponseEntity.ok(CommonResponse.success("상점이 " + status + "되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 상점을 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("상점 상태 변경 중 오류가 발생했습니다.");
		}
	}

	@GetMapping("/shop/OrderDetails")
	@ResponseBody
	@Operation(summary = "상품 구매수 모달", description = "해당 상품을 누가 구매한 정보를 볼 수 있다.")
	public ResponseEntity<CommonResponse<List<Map<String, Object>>>> fetchOrderDetails(
			@RequestParam("shopNo") Long shopNo) {
		try {
			List<OrderedEntity> orders = orderedRepository.findByShopNo(shopNo);

			if (orders.isEmpty()) {
				return ResponseEntity.ok(CommonResponse.success(Collections.emptyList()));
			}

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

			return ResponseEntity.ok(CommonResponse.success(orderDetailsList));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(CommonResponse.failure("주문 내역을 가져오는 중 오류가 발생했습니다."));
		}
	}
	
	//shop작성 
	@PostMapping("/shopWrite")
	@Operation(summary = "상점 작성", description = "상점을 작성할 수 있다.")
	public ResponseEntity<CommonResponse<String>> shopWrite(
	        @RequestPart("thumbnail") MultipartFile thumbnail, 
	        @RequestPart("srcImage") List<MultipartFile> descriptionImages,
	        @RequestPart("productData") String productDataJson) throws IOException {
	    
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        ShopDTO shopDTO = objectMapper.readValue(productDataJson, ShopDTO.class);

	        // 썸네일 이미지 업로드 처리
	        String shopMainPhotoUrl = shopService.uploadFile(thumbnail);

	        // 설명 이미지 업로드 처리
	        List<String> descriptionImageUrls = new ArrayList<>();
	        for (MultipartFile file : descriptionImages) {
	            String url = shopService.uploadFile(file);
	            descriptionImageUrls.add(url);
	        }

	        // 데이터 저장 로직 실행
	        shopService.shopWrite(shopDTO, shopMainPhotoUrl, descriptionImageUrls);

	        return ResponseEntity.ok(CommonResponse.success("상점이 성공적으로 작성되었습니다."));
	    } catch (NoSuchElementException e) {
	        throw new NoSuchElementException("상점 데이터를 처리하는 중 오류가 발생했습니다.");
	    } catch (Exception e) {
	        throw new RuntimeException("상점 작성 중 오류가 발생했습니다.");
	    }
	}

//-----------------------이벤트-------------------------------
	@DeleteMapping("/Event")
	@Operation(summary = "이벤트 삭제", description = "이벤트를 삭제한다.")
	public ResponseEntity<CommonResponse<String>> deleteEvent(@RequestParam("eventNo") Long eventNo) {
		if (eventNo == null || eventNo <= 0) {
			throw new IllegalArgumentException("유효하지 않은 이벤트 번호입니다.");
		}

		try {
			adminPageService.deleteEvent(eventNo);
			return ResponseEntity.ok(CommonResponse.success("이벤트가 성공적으로 삭제되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 이벤트를 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("이벤트 삭제 중 오류가 발생했습니다.");
		}
	}

//---------------------주문---------------------------------------
	@PostMapping("/refund/paymentToAdmin")
	@Operation(summary = "환불하기", description = "해당 유저의 구매한 항목을 환불한다.")
	public ResponseEntity<CommonResponse<String>> refundPayment(@RequestParam("merchantUId") String merchantUId,
			@RequestParam("refundReason") String refundReason, HttpSession session) throws Exception {

		String userid = (String) session.getAttribute("UId");
		String token = paymentService.getAccessToken();

		try {

			// 환불 처리
			paymentService.refundRequest(token, merchantUId);

			// 주문 상태 업데이트
			orderedService.updateOrderedState(merchantUId, refundReason, userid);

			// 관련 데이터 삭제 처리
			PayEntity pay = paymentService.findPayEntity(merchantUId);
			Long payNo = pay.getPayNo();
			PaymentAndUserService.deleteByPayNo(payNo); // payment_user_mapping 삭제
			paymentService.deleteByMerchantUId(merchantUId); // payment 삭제

			return ResponseEntity.ok(CommonResponse.success("환불 처리가 완료되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 주문을 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("환불 처리 중 오류가 발생했습니다.");
		}
	}

	@GetMapping("/refund/reason")
	@Operation(summary = "환불 이유", description = "해당 유저, 어드민이 환불한 이유에 대해 볼 수 있다.")
	public ResponseEntity<CommonResponse<Map<String, String>>> getRefundReason(
			@RequestParam("merchantUId") String merchantUId) {
		OrderedRefundEntity refundEntity = orderedRefundRepository.findByOrderedEntity_MerchantUId(merchantUId)
				.orElseThrow(() -> new NoSuchElementException("해당 주문에 대한 환불 정보를 찾을 수 없습니다."));

		Map<String, String> responseData = Map.of("refundReason", refundEntity.getRefundReason(), "refundUser",
				refundEntity.getRefundUser());

		return ResponseEntity.ok(CommonResponse.success(responseData));
	}

	@PatchMapping("/checkOdered")
	@Operation(summary = "주문 상태 변환", description = "유저의 주문을 확인하고 상태를 변경(주문확인)한다.")
	public ResponseEntity<CommonResponse<String>> updateShipmentState(@RequestParam("orderedNo") Long orderedNo,
			@RequestParam("shipmentState") String shipmentState) {

		try {
			// 주문 번호를 기반으로 주문을 조회하고 상태 업데이트
			adminPageService.updateShipmentState(orderedNo, shipmentState);

			// 성공적으로 업데이트된 경우 응답 반환
			return ResponseEntity.ok(CommonResponse.success("주문 상태가 성공적으로 업데이트되었습니다."));
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 주문을 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("주문 상태 업데이트 중 오류가 발생했습니다.");
		}
	}

//------------------문의 사항---------------------------------
	@GetMapping("/InquiryDetails")
	@ResponseBody
	@Operation(summary = "문의사항 모달창", description = "문의사항 모달창을 열어 답변을 본다.")
	public ResponseEntity<CommonResponse<InquiryDTO>> getInquiryDetails(@RequestParam("inqNo") Long inqNo) {
		InquiryEntity inquiryEntity = adminPageService.getInquiryById(inqNo);

		// 예외가 발생하면 GlobalExceptionHandler에서 처리됨 (NoSuchElementException 등)

		InquiryDTO inquiryDTO = new InquiryDTO();
		inquiryDTO.setInqNo(inquiryEntity.getInqNo());
		inquiryDTO.setInqTitle(inquiryEntity.getInqTitle());
		inquiryDTO.setInqRegisteredDate(inquiryEntity.getInqRegisteredDate());
		inquiryDTO.setInqCategory(inquiryEntity.getInqCategory());
		inquiryDTO.setInqContent(inquiryEntity.getInqContent());

		FindUserDTO findUserDTO = new FindUserDTO();
		findUserDTO.setUName(inquiryEntity.getUserEntity().getUName());
		findUserDTO.setUNo(inquiryEntity.getUserEntity().getUNo());
		findUserDTO.setUPofile(inquiryEntity.getUserEntity().getUPofile());
		inquiryDTO.setUser(findUserDTO);

		InquiryAnswerEntity answerEntity = adminPageService.getInquiryAnswerById(inqNo);
		if (answerEntity != null) {
			InquiryAnswerDTO answerDTO = new InquiryAnswerDTO();
			answerDTO.setAnsNo(answerEntity.getAnsNo());
			answerDTO.setAnsContent(answerEntity.getAnsContent());
			answerDTO.setAnsRegisteredDate(answerEntity.getAnsRegisteredDate());

			FindUserDTO answerUserDTO = new FindUserDTO();
			answerUserDTO.setUName(answerEntity.getUserEntity().getUName());
			answerUserDTO.setUPofile(answerEntity.getUserEntity().getUPofile());
			answerUserDTO.setUNo(answerEntity.getUserEntity().getUNo());
			answerDTO.setUser(answerUserDTO);

			inquiryDTO.setAnswer(answerDTO); // 답변이 있을 경우 설정
		} else {
			inquiryDTO.setAnswer(null); // 답변이 없을 경우 null
		}

		return ResponseEntity.ok(CommonResponse.success(inquiryDTO));
	}

	// 문의사항 모달창(답변보내기)
	@PostMapping("/Answer")
	@ResponseBody
	@Operation(summary = "문의사항 답변", description = "유저의 문의사항에 답변을 할 수 있다.")
	public ResponseEntity<CommonResponse<String>> submitAnswer(@RequestParam("inqNo") Long inqNo,
			@RequestParam("answerContent") String answerContent) {
		try {
			adminPageService.saveInquiryAnswer(inqNo, answerContent);
			return ResponseEntity.ok(CommonResponse.success("답변이 성공적으로 저장되었습니다."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(CommonResponse.failure("답변 저장에 실패했습니다."));
		}
	}

//--------------------------자주 묻는 질문 (faq)---------------------
	@GetMapping("/faq")
	@ResponseBody
	@Operation(summary = "자주 묻는 질문 수정(get)", description = "자주 묻는 질문의 수정 모달창을 띄울수 있다.")
	public ResponseEntity<CommonResponse<FaqDTO>> getFaq(@RequestParam("faqNo") Long faqNo) {
		FaqEntity faqEntity = adminPageService.getFaqById(faqNo);
		if (faqEntity != null) {
			// 엔티티를 DTO로 변환
			FaqDTO faqDTO = new FaqDTO();
			faqDTO.setFaqNo(faqEntity.getFaqNo());
			faqDTO.setFaqTitle(faqEntity.getFaqTitle());
			faqDTO.setFaqCategory(faqEntity.getFaqCategory());
			faqDTO.setFaqContent(faqEntity.getFaqContent());

			return ResponseEntity.ok(CommonResponse.success(faqDTO));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResponse.failure("FAQ를 찾을 수 없습니다."));
		}
	}

	@PatchMapping("/faq")
	@ResponseBody
	@Operation(summary = "자주 묻는 질문 수정(patch)", description = "선택한 자주 묻는 질문을 수정 할 수 있다.")
	public ResponseEntity<CommonResponse<String>> updateFaq(@RequestParam("faqNo") Long faqNo,
			@RequestBody FaqEntity faqData) {
		boolean updated = adminPageService.updateFaq(faqNo, faqData);

		if (updated) {
			return ResponseEntity.ok(CommonResponse.success("FAQ가 성공적으로 수정되었습니다."));
		} else {
			return ResponseEntity.badRequest().body(CommonResponse.failure("FAQ 수정에 실패했습니다."));
		}
	}

	@DeleteMapping("/faq")
	@ResponseBody
	@Operation(summary = "자주 묻는 질문 삭제", description = "선택한 자주 묻는 질문을 삭제 할 수 있다.")
	public ResponseEntity<CommonResponse<String>> deleteFaq(@RequestParam("faqNo") Long faqNo) {
		boolean deleted = adminPageService.deleteFaq(faqNo);

		if (deleted) {
			return ResponseEntity.ok(CommonResponse.success("FAQ가 성공적으로 삭제되었습니다."));
		} else {
			return ResponseEntity.badRequest().body(CommonResponse.failure("FAQ 삭제에 실패했습니다."));
		}
	}

}
