package com.my.interrior.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.common.MailDTO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@GetMapping("/auth/findPw")
	public ResponseEntity<String> findUPw(@ModelAttribute UserMailDTO dto,
			Model model) {
		String mail = dto.getUMail();
		String UName = dto.getUName();
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

	@GetMapping("/auth/check/{UId}")
	public ResponseEntity<String> checkDuplicatedId(@PathVariable("UId") String userId) {

		UserEntity user = userRepository.findByUId(userId);

		return ResponseEntity.ok(user != null ? "duplicated" : "available");

	}

	@GetMapping("/auth/check/email/{UMail}")
	public ResponseEntity<String> checkDuplicatedEmail(@PathVariable("UMail") String email) {

		UserEntity user = userRepository.findByUMail(email);

		return ResponseEntity.ok(user != null ? "duplicated" : "available");
	}

}
