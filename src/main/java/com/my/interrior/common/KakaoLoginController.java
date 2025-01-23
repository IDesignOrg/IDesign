/*package com.my.interrior.common;

import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;

@Controller
public class KakaoLoginController {
	
	@Autowired
	private KakaoLoginService kakaoService;
	
	@Autowired
	private UserRepository userRepository;

	//인증 시 코드 받는 곳
	@RequestMapping("/auth/login/kakao")
	public String kakaoLogin(@RequestParam("code") String code, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		System.out.println("code : " + code);
		String access_Token = kakaoService.getAccessToken(code);
		System.out.println("controller access_token : " + access_Token);
		HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
		System.out.println("login Contoroller : " + userInfo);
		
		
		String kakaoId = "kakao_" + Long.toString((Integer) userInfo.get("id"));
		UserEntity existingUser = userRepository.findByUId("kakao_"+kakaoId);
        
        if (existingUser == null) {
            // 새로운 사용자 정보 설정
            UserEntity newUser = new UserEntity();
            newUser.setUMail((String) userInfo.get("email"));
            newUser.setUName((String) userInfo.get("nickname"));
            newUser.setURegister(LocalDate.now());
            newUser.setUPw(""); // 카카오 로그인에서는 비밀번호를 사용하지 않으므로 빈 값으로 설정
            newUser.setUPofile((String) userInfo.get("profile"));
            newUser.setUBirth(""); // 생일 정보는 카카오에서 따로 제공하지 않으므로 비워둠
            newUser.setUTel(""); // 전화번호 정보도 카카오에서 따로 제공하지 않으므로 비워둠
            newUser.setUId("kakao_"+kakaoId);
            
            userRepository.save(newUser); // 새로운 사용자 저장
            session.setAttribute("access_Token", access_Token);
            session.setAttribute("UId", newUser.getUId()); // 세션에 새로운 사용자 ID 설정
        }else {
        	if (existingUser.isUDeactivated()) {
                // 비활성화된 계정일 경우 메시지만 전달
        		redirectAttributes.addFlashAttribute("deactivatedError", "비활성화된 아이디입니다. 복구신청을 하시겠습니까?");
				redirectAttributes.addFlashAttribute("userNo", existingUser.getUNo());
                return "redirect:/auth/login";
            }
        	session.setAttribute("access_Token", access_Token);
        	session.setAttribute("UId", existingUser.getUId());
        }
		
		return "redirect:/";
	}
}
*/