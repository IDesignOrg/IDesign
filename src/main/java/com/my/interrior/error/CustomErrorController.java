package com.my.interrior.error;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController {

	@GetMapping("/error/401")
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String unauthorized(HttpServletRequest request) {
		String scheme = request.getScheme();
	    if (scheme.equals("http")) {
	        // HTTP로 접근할 경우 HTTPS로 리다이렉트
	        return "redirect:https://idesign.r-e.kr/error/401";
	    }
	    return "client/login";
	}
}
