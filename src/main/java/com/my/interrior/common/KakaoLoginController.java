package com.my.interrior.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KakaoLoginController {

	//인증 시 코드 받는 곳
	@RequestMapping("/auth/login/kakao")
	public String kakaoLogin(@RequestParam("code") String code) {
		
		
		return "client/join";
	}
}
