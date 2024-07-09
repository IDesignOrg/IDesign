package com.my.interrior.common;

import java.time.LocalDate;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String kakaoLogin(@RequestParam("code") String code, HttpSession session) {
		System.out.println("code : " + code);
		String access_Token = kakaoService.getAccessToken(code);
		System.out.println("controller access_token : " + access_Token);
		HashMap<String, Object> userInfo = kakaoService.getUserInfo(access_Token);
		System.out.println("login Contoroller : " + userInfo);
		
		if(userInfo.get("email") != null) {
			session.setAttribute("UId", userInfo.get("email"));
	        session.setAttribute("access_Token", access_Token);
	        UserEntity newUser = new UserEntity();
	        newUser.setUMail((String) userInfo.get("email"));
	        newUser.setUName((String) userInfo.get("nickname")); // 사용자 이름은 카카오에서 제공하는 닉네임으로 설정
	        newUser.setURegister(LocalDate.now());
	        newUser.setUPw(""); // 카카오 로그인에서는 비밀번호를 사용하지 않으므로 빈 값으로 설정
	        newUser.setUPofile((String) userInfo.get("profile"));
	        newUser.setUBirth(""); // 생일 정보는 카카오에서 따로 제공하지 않으므로 비워둠
	        newUser.setUTel(""); // 전화번호 정보도 카카오에서 따로 제공하지 않으므로 비워둠
	        
	        // 카카오 제공하는 고유 ID를 UId로 설정할 때, 앞에 kakao_ 접두어 추가
	        newUser.setUId("kakao_" + Long.toString((Integer) userInfo.get("id")));
	        
	        userRepository.save(newUser); // 사용자 저장
	        
	        session.setAttribute("UId", newUser.getUId()); // 세션에 사용자 ID 설정
	       System.out.println();
		}
		
		return "redirect:/";
	}
}
