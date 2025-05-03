package com.my.interrior.interceptor;

import com.my.interrior.common.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class Interceptor implements HandlerInterceptor {

	@Autowired
	private SessionManager sessionManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 세션 매니저를 통한 세션 유효성 검증
		if (!sessionManager.validateSession(request)) {
			response.sendRedirect("/error/401");  // 401 페이지로 리다이렉트
			return false;
		}
		return true;
	}
}