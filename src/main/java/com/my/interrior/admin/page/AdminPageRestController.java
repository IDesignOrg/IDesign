package com.my.interrior.admin.page;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.admin.coupon.CouponMapRepository;
import com.my.interrior.admin.coupon.CouponService;
import com.my.interrior.client.evaluation.ReviewEntity;
import com.my.interrior.client.evaluation.ReviewService;
import com.my.interrior.client.evaluation.DTO.ReviewCommentDTO;
import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.shop.ShopReviewEntity;
import com.my.interrior.client.shop.ShopReviewRepository;
import com.my.interrior.client.shop.ShopService;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserService;
import com.my.interrior.common.CommonResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
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

//-----------------------어드민 리뷰 페이지----------------------
	// admin페이지 리뷰 댓글 보기
	@GetMapping("/getRComments")
	public ResponseEntity<CommonResponse<List<ReviewCommentDTO>>> fetchRComments(
			@RequestParam("reviewNo") Long reviewNo) {
		List<ReviewCommentDTO> comments = reviewService.getCommentsByReviewId(reviewNo).stream()
				.map(comment -> new ReviewCommentDTO(comment.getRCommentNo(), comment.getRComment(),
						comment.getRCommentCreated(), comment.getUser().getUName(), comment.getUser().getUPofile()))
				.collect(Collectors.toList());

		return ResponseEntity.ok(CommonResponse.success(comments));
	}

	// 리뷰페이지에서 리뷰 삭제
	@DeleteMapping("/deleteReview")
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
	@GetMapping("/getPosts")
	@ResponseBody
	public ResponseEntity<CommonResponse<List<ReviewEntity>>> fetchPosts(@RequestParam("userUNo") Long userUNo) {
		List<ReviewEntity> reviews = adminPageService.getPostsByUser(userUNo);
		System.out.println("일단 들어");
		return ResponseEntity.ok(CommonResponse.success(reviews));
	}

	// 어드민 유저 페이지 게시글 모달 삭제
	@DeleteMapping("/deletePost")
	public ResponseEntity<CommonResponse<String>> deletePost(@RequestParam("rNo") Long rNo) {
		reviewService.deleteReview(rNo);
		return ResponseEntity.ok(CommonResponse.success("게시글이 성공적으로 삭제되었습니다."));
	}

	// 유저 비활성화
	@PatchMapping("/deactivateUser")
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
	@PostMapping("/issueCouponToUser")
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
	@DeleteMapping("/deleteShopComment")
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
	
	@GetMapping("/fetchComments")
	@ResponseBody
	public ResponseEntity<CommonResponse<List<ShopReviewEntity>>> fetchComments(@RequestParam("userUNo") Long userUNo) {
	    if (userUNo == null || userUNo <= 0) {
	        throw new IllegalArgumentException("유효하지 않은 사용자 번호입니다.");
	    }

	    try {
	        List<ShopReviewEntity> comments = shopReviewRepository.findByUserUNo(userUNo);
	        if (comments.isEmpty()) {
	            return ResponseEntity.ok(CommonResponse.success(Collections.emptyList()));
	        }
	        return ResponseEntity.ok(CommonResponse.success(comments));
	    } catch (NoSuchElementException e) {
	        throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다.");
	    }
	}



}
