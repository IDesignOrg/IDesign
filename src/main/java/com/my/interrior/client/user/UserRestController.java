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

@RestController
public class UserRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	@GetMapping("/auth/findPw/{UName}")
	public ResponseEntity<String> findUPw(@ModelAttribute UserMailDTO dto, @PathVariable("UName") String UName,
			Model model) {
		String mail = dto.getUMail();
		String name = dto.getUName();

		MailDTO user = userService.checkMailAndName(mail, name);

		if (user != null) {

			userService.mailSend(user);
			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
		}

	}

	@GetMapping("/auth/check/{UId}")
	public ResponseEntity<String> checkDuplicatedId(@PathVariable("UId") String userId) {

		UserEntity user = userRepository.findByUId(userId);

		return ResponseEntity.ok(user != null ? "duplicated" : "available");

	}
}
