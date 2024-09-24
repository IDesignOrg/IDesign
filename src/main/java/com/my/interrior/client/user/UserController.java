package com.my.interrior.client.user;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my.interrior.common.GoogleApi;
import com.my.interrior.common.KakaoApi;
import com.my.interrior.common.NaverApi;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
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

	@Autowired
	private NaverApi naverApi;

	@Autowired
	private HttpSession session;

	@GetMapping("/signin")
	public String LoginPage(Model model) {

		model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
		model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
		model.addAttribute("googleClientId", googleApi.getClientId());
		model.addAttribute("googleRedirectUri", googleApi.getRedirectUri());
		model.addAttribute("googleScope", googleApi.getScope());
		model.addAttribute("naverClientId", naverApi.getClientId());
		model.addAttribute("naverRedirectUri", naverApi.getRedirectUri());

		System.out.println("googleClientId: " + googleApi.getClientId());
		System.out.println("googleRedirectUri: " + googleApi.getRedirectUri());
		System.out.println("googleScope: " + googleApi.getScope());
		return "client/login";
	}

	@GetMapping("/signup")
	public String join(Model model) {
		model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
		model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
		model.addAttribute("googleClientId", googleApi.getClientId());
		model.addAttribute("googleRedirectUri", googleApi.getRedirectUri());
		model.addAttribute("googleScope", googleApi.getScope());
		model.addAttribute("naverClientId", naverApi.getClientId());
		model.addAttribute("naverRedirectUri", naverApi.getRedirectUri());
		return "client/join";
	}

	@PostMapping("/signup")
	public String join(@ModelAttribute UserEntity userEntity) throws Exception {
		try {
			userEntity.setURegister(LocalDate.now());
			userEntity.setUPw(passwordEncoder.encode(userEntity.getUPw()));
			userEntity.setUPofile("https://storage.googleapis.com/idesign/static/blank-profile-picture-973460_640.png");
			if (userEntity.getURegister() == null || userEntity.getUPw() == null)
				return null;
			// insert
			userRepository.save(userEntity);

			return "redirect:/signin";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	@PostMapping("/signin")
	public String login(@ModelAttribute UserDTO userDTO, HttpSession session, Model model,  RedirectAttributes redirectAttributes) throws Exception {
		try {
			if (userDTO.getUId() == null || userDTO.getUPw() == null)
				return "redirect:/signin";

			String UId = userDTO.getUId();
			String UPw = userDTO.getUPw();
			UserEntity user = userService.checkLogin(UId);
			System.out.println("user" + user);
			if (user != null && passwordEncoder.matches(UPw, user.getUPw())) {
				if (user.isUDeactivated()) {
					redirectAttributes.addFlashAttribute("deactivatedError", "비활성화된 아이디입니다. 복구신청을 하시겠습니까?");
					redirectAttributes.addFlashAttribute("userNo", user.getUNo());
	                return "redirect:/signin";
				}
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

	@GetMapping("/forgot-id")
	public String findPasswordPage() {
		return "client/findUId";
	}

	@GetMapping("/forgot-password")
	public String findUPw() {
		return "client/findUPw";
	}

	// 로그아웃
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		System.out.println("로그아웃 됨");
		return "redirect:/";
	}
}
