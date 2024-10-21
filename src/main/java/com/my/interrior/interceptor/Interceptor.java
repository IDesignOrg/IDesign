package com.my.interrior.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class Interceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 요청이 컨트롤러에 도달하기 전에 실행되는 코드
		HttpSession session = request.getSession(false); // 세션에 없으면 널값반환

		if (session == null || session.getAttribute("UId") == null) {
			response.sendRedirect("/error/401");  // 401 페이지로 리다이렉트
            return false;
		}
		return true;
	}

}
