package com.my.interrior.client.evaluation;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.my.interrior.client.evaluation.DTO.ReviewCommentDTO;
import com.my.interrior.client.evaluation.DTO.ReviewCommentRequest;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.client.user.UserRestController;
import com.my.interrior.common.CommonResponse;

import io.swagger.v3.oas.annotations.Parameter;
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
	public ResponseEntity<CommonResponse<ReviewCommentDTO>> addComment(
			@RequestBody ReviewCommentRequest request,
			HttpSession session) {

		// 세션에서 userId를 가져옴
		String userId = (String) session.getAttribute("UId");
		if (userId == null) {
			throw new NoSuchElementException("일치하는 회원 정보가 없습니다."); // 로그인되지 않은 경우
		}

		// 서비스에서 댓글을 추가하고 DTO를 반환받음
		ReviewCommentDTO commentDTO = reviewService.addComment(request.getReviewId(), userId, request.getComment());

		// 클라이언트에 DTO 반환
		return ResponseEntity.ok(CommonResponse.success(commentDTO));
	}

}
