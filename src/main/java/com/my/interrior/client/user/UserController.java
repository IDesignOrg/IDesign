package com.my.interrior.client.user;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.my.interrior.common.GoogleApi;
import com.my.interrior.common.KakaoApi;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository; // JpaRepository(save, deleteBy)

	@Autowired
	private UserService userService;

	@Autowired
	private KakaoApi kakaoApi;

	@Autowired
	private GoogleApi googleApi;

	@GetMapping("/auth/login")
	public String LoginPage(Model model) {

		model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
		model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
		model.addAttribute("googleClientId", googleApi.getClientId());
		model.addAttribute("googleRedirectUri", googleApi.getRedirectUri());
		model.addAttribute("googleScope", googleApi.getScope());

		System.out.println("googleClientId: " + googleApi.getClientId());
		System.out.println("googleRedirectUri: " + googleApi.getRedirectUri());
		System.out.println("googleScope: " + googleApi.getScope());
		return "client/login";
	}

	@GetMapping("/auth/join")
	public String join() {
		return "client/join";
	}

	@PostMapping("/auth/join")
	public String join(@ModelAttribute UserEntity userEntity) throws Exception {
		try {
			userEntity.setURegister(LocalDate.now());
			userEntity.setUPw(passwordEncoder.encode(userEntity.getUPw()));
			if (userEntity.getURegister() == null || userEntity.getUPw() == null)
				return null;
			// insert
			userRepository.save(userEntity);

			return "redirect:/auth/login";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@PostMapping("/auth/login")
	public String login(@ModelAttribute UserDTO userDTO, HttpSession session, Model model) throws Exception {
		try {
			if (userDTO.getUId() == null || userDTO.getUPw() == null)
				return "redirect:/auth/login";

			String UId = userDTO.getUId();
			String UPw = userDTO.getUPw();
			UserEntity user = userService.checkLogin(UId);
			System.out.println("user" + user);
			if (user != null && passwordEncoder.matches(UPw, user.getUPw())) {
				session.setAttribute("UId", user.getUId());
				return "redirect:/";
			} else {
				model.addAttribute("loginError", "입력하신 정보를 확인하세요.");
				return "client/login";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	@GetMapping("/auth/findUId")
	public String findPasswordPage() {
		return "client/findUId";
	}

	@PostMapping("/auth/findUserID")
	public ResponseEntity<String> findUserID(@RequestBody Map<String, String> requestData) {
		String UPw = requestData.get("UPw");
		String UMail = requestData.get("UMail");

		UserEntity user = userRepository.findByUPwAndUMail(UPw, UMail);

		System.out.println("유저" + user);

		if (user != null) {
			String ID = user.getUId();
			return ResponseEntity.ok(ID);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
