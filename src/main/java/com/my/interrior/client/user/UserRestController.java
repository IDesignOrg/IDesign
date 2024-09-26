package com.my.interrior.client.user;

import java.util.NoSuchElementException;

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
import com.my.interrior.common.DefaultApiResponse;
import com.my.interrior.common.MailDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

	@DefaultApiResponse
	@Transactional
	@GetMapping("/forgot-password")
	@Operation(summary = "비밀번호 찾기", description = "회원의 비밀번호를 찾습니다.")
	public ResponseEntity<CommonResponse<String>> findUPw(
			@Parameter(name = "dto", description = "이메일과 이름을 받는 DTO")
			@ModelAttribute UserMailDTO dto, Model model) {
		String mail = dto.getMail();
		String UName = dto.getName();
		log.info("mail: {}, name :{}", mail);
		// mail을 입력 안 했을 때
		// 이름을 입력 안 했을 때
		if (UName == null || UName.isEmpty())
			throw new IllegalArgumentException("이름이 입력되지 않았습니다.");

		if (mail == null || mail.isEmpty())
			throw new IllegalArgumentException("이메일이 입력되지 않았습니다.");

		MailDTO user = userService.checkMailAndName(mail, UName);

		// 맞는 데이터가 없을 때
		if (user == null)
			throw new NoSuchElementException("일치하는 회원 정보가 없습니다.");

		userService.mailSend(user);
		return ResponseEntity.ok(CommonResponse.success("success"));
	}

	@DefaultApiResponse
	@Operation(summary = "아이디 중복 확인")
	@GetMapping("/check/id/{userID}")
	public ResponseEntity<String> checkDuplicatedId(
			@Parameter(name = "userID", description = "아이디를 받습니다.") @PathVariable("userID") String userId) throws Exception {

		UserEntity user = userRepository.findByUId(userId);

		return ResponseEntity.ok(user != null ? "duplicated" : "available");

	}

	@DefaultApiResponse
	@Operation(summary = "이메일 중복 확인")
	@GetMapping("/check/mail/{mail}")
	public ResponseEntity<CommonResponse<String>> checkDuplicatedEmail(
			@Parameter(name = "mail", description = "이메일을 받습니다.") @PathVariable("mail") String email) throws Exception {

		UserEntity user = userRepository.findByUMail(email);

		return ResponseEntity
				.ok(user != null ? CommonResponse.success("duplicated") : CommonResponse.success("available"));
	}

	// 유저 비활성화
	@DefaultApiResponse
	@Operation(summary = "유저 비활성화", description = "유저 탈퇴시 비활성화")
	@PatchMapping("/user/{userNo}/disabled")
	public ResponseEntity<CommonResponse<String>> deactivateUser(
			@Parameter(name = "userNo", description = "user-number를 받습니다.")
			@PathVariable("userNo") Long userNo, Model model,
			HttpSession session) throws Exception {

		// 사용자 비활성화 서비스 호출
		boolean isDeactivated = userService.deactivateUser(userNo);

		if (isDeactivated) {
			// 유저가 비활성화되면 리다이렉트
			session.invalidate();
			return ResponseEntity.ok().body(CommonResponse.success("success"));
		} else {
			// 유저 비활성화 실패 시 메시지를 추가하고 현재 페이지에 머무르도록 설정
			throw new NoSuchElementException("해당 정보가 없습니다.");
		}
	}

	@DefaultApiResponse
	@GetMapping("/forgot-id")
	@Operation(summary = "아이디 찾기", description = "회원의 아이디를 찾습니다.")
	public ResponseEntity<CommonResponse<String>> findUserID(
			@Parameter(name = "mail", description = "이메일을 받습니다.") @RequestParam("mail") String UMail) throws Exception {

		if (UMail == null || UMail.isEmpty())
			throw new IllegalArgumentException("이메일을 입력해주세요.");

		UserEntity user = userRepository.findByUMail(UMail);

		if (user != null) {
			String ID = user.getUId();
			log.info("ID: {}", ID);
			return ResponseEntity.ok(CommonResponse.success(ID));
		} else {
			throw new NoSuchElementException("회원 정보가 없습니다.");
		}
	}
}
