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

//import com.my.interrior.common.KakaoApi;
import com.my.interrior.common.NaverApi;
import com.my.interrior.config.LoginAttemptService;

import jakarta.servlet.http.HttpServletRequest;
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
	private LoginAttemptService loginAttemptService;

	//@Autowired
	//private KakaoApi kakaoApi;

	@Autowired
	private NaverApi naverApi;

	@Autowired
	private HttpSession session;

	@GetMapping("/signin")
	public String LoginPage(Model model) {

		//model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
		//model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
		//model.addAttribute("googleClientId", googleApi.getClientId());
		//model.addAttribute("googleRedirectUri", googleApi.getRedirectUri());
		//model.addAttribute("googleScope", googleApi.getScope());
		model.addAttribute("naverClientId", naverApi.getClientId());
		model.addAttribute("naverRedirectUri", naverApi.getRedirectUri());

		return "client/login";
	}

	@GetMapping("/signup")
	public String join(Model model) {
		//model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
		//model.addAttribute("redirectUri", kakaoApi.getKakaoRedirectUri());
		//model.addAttribute("googleClientId", googleApi.getClientId());
		//model.addAttribute("googleRedirectUri", googleApi.getRedirectUri());
		//model.addAttribute("googleScope", googleApi.getScope());
		model.addAttribute("naverClientId", naverApi.getClientId());
		model.addAttribute("naverRedirectUri", naverApi.getRedirectUri());
		return "client/join";
	}

	@PostMapping("/signup")
	public String join(@ModelAttribute UserEntity userEntity){
		try {
			userEntity.setURegister(LocalDate.now());
			userEntity.setUPw(passwordEncoder.encode(userEntity.getUPw()));
			userEntity.setUPofile("https://storage.googleapis.com/idesign5/static/Default-Profile-Picture-PNG-Download-Image.png");
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
	public String login(@ModelAttribute UserDTO userDTO, Model model,  RedirectAttributes redirectAttributes, HttpServletRequest request){
		
		//ip와 id값을 key로 생성
		String key = request.getRemoteAddr();
		if (userDTO.getUId() != null && !userDTO.getUId().isEmpty()) {
			key = key + "-" + userDTO.getUId();
		}
		
		//차단 여부 확인
		//차단 횟수가 5회가 되면 해당 메서드에서 차단(true)
		if(loginAttemptService.isBlocked(key)) {
			//차단 시간을 가져와서 model에 뿌림
			long remainingMinute = loginAttemptService.getRemainingBlockTime(key);
			model.addAttribute("loginError", "로그인 시도 횟수를 초과했습니다. " + remainingMinute + "분 후에 다시 시도해주세요.");
			return "client/login";
			
		}
		
		try {
			if (userDTO.getUId() == null || userDTO.getUPw() == null)
				return "redirect:/signin";

			String UId = userDTO.getUId();
			String UPw = userDTO.getUPw();
			UserEntity user = userService.checkLogin(UId);

			if (user != null && passwordEncoder.matches(UPw, user.getUPw())) {
				if (user.isUDeactivated()) {
					redirectAttributes.addFlashAttribute("deactivatedError", "비활성화된 아이디입니다. 복구신청을 하시겠습니까?");
					redirectAttributes.addFlashAttribute("userNo", user.getUNo());
	                return "redirect:/signin";
				}
				
				//기존 세션 무효화
				HttpSession oldSession = request.getSession(false);
				if(oldSession != null) {
					oldSession.invalidate();
				}
				
				//새 세션 생성 및 정보 저장
				HttpSession newSession = request.getSession(true);
				newSession.setAttribute("UId", user.getUId());
				
				return "redirect:/";
			} else {
				//로그인 실패시 실패 횟수 증가
				loginAttemptService.loginFailed(key);
				//남은 로그인 횟수 확인
				int attemptsLeft = loginAttemptService.getAttemptsLeft(key);
				
				model.addAttribute("loginError", "입력하신 정보를 확인하세요. 남은 시도 횟수: " + attemptsLeft + "회");
				return "client/login";
			}
		} catch (Exception e) {
			// 에러가 나오 로그인 횟수 증가
			loginAttemptService.loginFailed(key);
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
