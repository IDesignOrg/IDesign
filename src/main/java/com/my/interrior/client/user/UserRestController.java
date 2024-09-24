package com.my.interrior.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.common.CommonResponse;
import com.my.interrior.common.MailDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Account", description = "Account API")
@RestController
@Slf4j
@RequestMapping("/api")
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@GetMapping("/forgot-password")
	@Operation(summary = "비밀번호 찾기", description = "회원의 비밀번호를 찾습니다.")
	@ApiResponse(responseCode = "200", description = "성공")
	@ApiResponse(responseCode = "400", description = "잘못된 요청: 이메일 또는 이름이 입력되지 않음.")
	@ApiResponse(responseCode = "404", description = "일치하는 회원 정보가 없음.")
	public ResponseEntity<CommonResponse<String>> findUPw(
			@Parameter(name = "dto", description = "이메일과 이름을 받는 DTO")
			@ModelAttribute UserMailDTO dto,
			Model model) {
		String mail = dto.getMail();
		String UName = dto.getName();
		log.info("mail: {}, name :{}", mail);
		// mail을 입력 안 했을 때
		// 이름을 입력 안 했을 때
		if (UName == null || UName.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.failure("이름이 입력되지 않았습니다."));

		if (mail == null || mail.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponse.failure("이메일이 입력되지 않았습니다."));

		MailDTO user = userService.checkMailAndName(mail, UName);

		// 맞는 데이터가 없을 때
		if (user == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResponse.failure("일치하는 회원 정보가 없습니다."));

		userService.mailSend(user);
		return ResponseEntity.ok(CommonResponse.success("success"));
	}

	@GetMapping("/check/id/{userID}")
	public ResponseEntity<String> checkDuplicatedId(@PathVariable("userID") String userId) {

		UserEntity user = userRepository.findByUId(userId);

		return ResponseEntity.ok(user != null ? "duplicated" : "available");

	}

	@GetMapping("/check/mail/{mail}")
	public ResponseEntity<String> checkDuplicatedEmail(@PathVariable("mail") String email) {

		UserEntity user = userRepository.findByUMail(email);

		return ResponseEntity.ok(user != null ? "duplicated" : "available");
	}

	// 유저 비활성화
	@PatchMapping("/user/{userNo}/disabled")
	public ResponseEntity<?> deactivateUser(@PathVariable("userNo") Long userNo, Model model, HttpSession session) {
		try {
			// 사용자 비활성화 서비스 호출
			boolean isDeactivated = userService.deactivateUser(userNo);

			if (isDeactivated) {
				// 유저가 비활성화되면 리다이렉트
				session.invalidate();
				return ResponseEntity.ok().body("success");
			} else {
				// 유저 비활성화 실패 시 메시지를 추가하고 현재 페이지에 머무르도록 설정
				return ResponseEntity.notFound().build(); // 오류 처리용 뷰 페이지로 변경 가능
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build(); // 서버 오류 처리용 뷰 페이지로 변경 가능
		}
	}

	@GetMapping("/forgot-id")
	public ResponseEntity<String> findUserID(@RequestParam("mail") String UMail) throws Exception {

		if (UMail == null || UMail.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NoMail");

		UserEntity user = userRepository.findByUMail(UMail);

		if (user != null) {
			String ID = user.getUId();
			log.info("ID: {}", ID);
			return ResponseEntity.ok(ID);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("fail");
		}
	}
}
