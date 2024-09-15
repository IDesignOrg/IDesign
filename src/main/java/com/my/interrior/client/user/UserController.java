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

	@GetMapping("/auth/login")
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

	@GetMapping("/auth/join")
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

	@PostMapping("/auth/join")
	public String join(@ModelAttribute UserEntity userEntity) throws Exception {
		try {
			userEntity.setURegister(LocalDate.now());
			userEntity.setUPw(passwordEncoder.encode(userEntity.getUPw()));
			userEntity.setUPofile("https://storage.googleapis.com/idesign/static/blank-profile-picture-973460_640.png");
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
				if (user.isUDeactivated()) {
					model.addAttribute("loginError", "비활성화된 아이디입니다.");
					return "client/login";
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

	@GetMapping("/auth/findUId")
	public String findPasswordPage() {
		return "client/findUId";
	}

	@GetMapping("/auth/findUPw")
	public String findUPw() {
		return "client/findUPw";
	}

	@GetMapping("/auth/find/user/id")
	public ResponseEntity<String> findUserID(@RequestParam("UMail") String UMail) throws Exception {

		if(UMail == null || UMail.isEmpty())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("NoMail");
		
		UserEntity user = userRepository.findByUMail(UMail);

		if (user != null) {
			String ID = user.getUId();
			return ResponseEntity.ok(ID);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NoData");
		}
	}

	// 로그아웃
	@RequestMapping("/logout")
	public String logout() {
		session.invalidate();
		System.out.println("로그아웃 됨");
		return "redirect:/";
	}

	// 유저 비활성화
	@PostMapping("/deactivateUserin")
	public String deactivateUser(@RequestParam("userUNo") Long userUNo, Model model) {
	    try {
	        // 사용자 비활성화 서비스 호출
	        boolean isDeactivated = userService.deactivateUser(userUNo);

	        if (isDeactivated) {
	            // 유저가 비활성화되면 리다이렉트
	        	session.invalidate();
	            return "redirect:/";
	        } else {
	            // 유저 비활성화 실패 시 메시지를 추가하고 현재 페이지에 머무르도록 설정
	            model.addAttribute("error", "유저 비활성화에 실패했습니다.");
	            return "client/error"; // 오류 처리용 뷰 페이지로 변경 가능
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "서버 오류로 인해 유저 비활성화에 실패했습니다.");
	        return "client/error"; // 서버 오류 처리용 뷰 페이지로 변경 가능
	    }
	}

}
