package com.my.interrior.client.evaluation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.client.evaluation.DTO.ReviewCommentDTO;
import com.my.interrior.client.evaluation.DTO.ReviewCommentRequest;
import com.my.interrior.client.evaluation.DTO.ReviewDTO;
import com.my.interrior.common.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Review", description = "Review API")
@RestController
@Slf4j
@RequestMapping("/api")
public class ReviewRestController {

	@Autowired
	private ReviewService reviewService;

	// 리뷰 댓글 쓰기
	@PostMapping("/review/comment")
	@Operation(summary = "댓글 작성", description = "리뷰에 댓글을 작성한다. `userId`는 세션에서 자동으로 가져온다.")
	public ResponseEntity<CommonResponse<ReviewCommentDTO>> addComment(@RequestBody ReviewCommentRequest request,
			HttpSession session) {

		// 세션에서 userId를 가져옴
		String userId = (String) session.getAttribute("UId");
		if (userId == null) {
			throw new NoSuchElementException("일치하는 회원 정보가 없습니다."); // 로그인되지 않은 경우
		}

		try {
			// 서비스에서 댓글을 추가하고 DTO를 반환받음
			ReviewCommentDTO commentDTO = reviewService.addComment(request.getReviewId(), userId, request.getComment());

			// 클라이언트에 DTO 반환
			return ResponseEntity.ok(CommonResponse.success(commentDTO));
		} catch (Exception e) {
			throw new IllegalArgumentException("댓글 추가 중 오류가 발생했습니다.");
		}
	}

	@GetMapping("/getReviewDetail")
	@ResponseBody
	@Operation(summary = "리뷰 상세 조회", description = "리뷰 번호를 기준으로 리뷰와 리뷰의 사진, 댓글 데이터를 조회한다.")
	public ResponseEntity<CommonResponse<ReviewDTO>> getReviewDetail(@RequestParam("rNo") Long rNo,
			HttpSession session) {
		try {
			// 조회수 증가 로직
			String pageKey = "viewedShop_" + rNo;
			LocalDateTime lastViewedTime = (LocalDateTime) session.getAttribute(pageKey);
			if (lastViewedTime == null || lastViewedTime.isBefore(LocalDateTime.now().minusHours(1))) {
				reviewService.increaseViewCount(rNo);
				session.setAttribute(pageKey, LocalDateTime.now());
			}

			// 리뷰 조회
			Optional<ReviewEntity> review = reviewService.getReviewById(rNo);
			if (!review.isPresent()) {
				throw new NoSuchElementException("해당 리뷰를 찾을 수 없습니다.");
			}

			// 리뷰에 첨부된 사진과 댓글 조회
			List<String> reviewPhotos = reviewService.getPhotosByReviewId(rNo).stream()
					.map(ReviewPhotoEntity::getRpPhoto).collect(Collectors.toList());

			List<ReviewCommentDTO> comments = reviewService.getCommentsByReviewId(rNo).stream()
					.map(comment -> new ReviewCommentDTO(
							comment.getRCommentNo(),
							comment.getRComment(),
							comment.getRCommentCreated(),
							comment.getUser().getUName(),
							comment.getUser().getUId(),
							comment.getUser().getUPofile()))
					.collect(Collectors.toList());

			// DTO 구성
			ReviewDTO response = new ReviewDTO(review.get().getRNo(), review.get().getRTitle(),
					review.get().getRContent(), review.get().getRCategory(), review.get().getRStarRating(),
					review.get().getRViews(), review.get().getRWrittenTime(), review.get().getUser().getUId(),
					reviewPhotos, comments);

			return ResponseEntity.ok(CommonResponse.success(response)); // 성공적으로 데이터를 반환

		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("해당 리뷰를 찾을 수 없습니다.");
		} catch (Exception e) {
			throw new RuntimeException("리뷰 상세 조회 중 오류가 발생했습니다.");
		}
	}

	// 리뷰 삭제
	@DeleteMapping("/review/comment/{commentId}")
	@Operation(summary = "댓글 삭제", description = "리뷰에 달린 댓글을 삭제한다.")
	public ResponseEntity<CommonResponse<String>> deleteComment(@PathVariable("commentId") Long commentId) {
		System.out.println("리뷰 삭제에 들어옴");
	    try {
	        // 댓글 삭제 로직 호출
	        reviewService.deleteComment(commentId);
	        return ResponseEntity.ok(CommonResponse.success("댓글이 성공적으로 삭제되었습니다."));
	    } catch (NoSuchElementException e) {
	        // 해당 댓글이 없을 때 예외 처리
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body(CommonResponse.failure("해당 댓글을 찾을 수 없습니다."));
	    } catch (Exception e) {
	        // 그 외 예외 처리
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(CommonResponse.failure("댓글 삭제 중 오류가 발생했습니다."));
	    }
	}


}
