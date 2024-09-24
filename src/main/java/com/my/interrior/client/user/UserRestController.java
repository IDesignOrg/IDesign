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

import com.my.interrior.common.MailDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

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
	public ResponseEntity<String> findUPw(@ModelAttribute UserMailDTO dto, Model model) {
		String mail = dto.getMail();
		String UName = dto.getName();
		log.info("mail: {}, name :{}", mail);
		// mail을 입력 안 했을 때
		if (mail == null || mail.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NoMail");
		// 이름을 입력 안 했을 때
		if (UName == null || UName.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NoName");

		MailDTO user = userService.checkMailAndName(mail, UName);

		// 맞는 데이터가 없을 때
		if (user == null)
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NoData");

		userService.mailSend(user);
		return ResponseEntity.ok("success");
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
}
