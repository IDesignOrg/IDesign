package com.my.interrior.common;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;

@Controller
public class KakaoLoginController {
	
	@Autowired
	private KakaoLoginService kakaoService;

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
		}
		
		return "redirect:/";
	}
}
