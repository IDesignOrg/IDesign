package com.my.interrior.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component //일단 찾아라 컴포넌트다 ~ 인터셉터 확인 가능할거다 라는 의미
public class Interceptor implements HandlerInterceptor {

    @Override

    public boolean preHandle(HttpServletRequest  request, HttpServletResponse response, Object handler) throws Exception {
        // 요청이 컨트롤러에 도달하기 전에 실행되는 코드

        HttpSession session = request.getSession(false); //세션에 없으면 널값반환


        if (session == null || session.getAttribute("UId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println("권한이 필요합니다.");
            return false;
        }
        System.out.println("권한 있음.");
        return true;
    }

}
