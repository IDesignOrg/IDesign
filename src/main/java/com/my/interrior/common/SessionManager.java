package com.my.interrior.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class SessionManager {

    private static final String USER_ID_ATTR = "UId";
    private static final String USER_AGENT_ATTR = "userAgent";
    private static final String LOGIN_IP_ATTR = "loginIp";
    private static final String LOGIN_TIME_ATTR = "loginTime";

    // 로그인 성공 시 세션 설정
    public void createUserSession(HttpServletRequest request, String userId) {
        // 기존 세션이 있다면 무효화
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // 새 세션 생성
        HttpSession newSession = request.getSession(true);

        // 사용자 정보 저장
        newSession.setAttribute(USER_ID_ATTR, userId);

        // 세션 추적을 위한 추가 정보 저장
        newSession.setAttribute(USER_AGENT_ATTR, request.getHeader("User-Agent"));
        newSession.setAttribute(LOGIN_IP_ATTR, request.getRemoteAddr());
        newSession.setAttribute(LOGIN_TIME_ATTR, LocalDateTime.now());

    }

    // 세션 유효성 검증
    public boolean validateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_ID_ATTR) == null) {
            return false;
        }

        // 추가 보안 검증: User-Agent가 변경되었는지 확인
        String currentUserAgent = request.getHeader("User-Agent");
        String sessionUserAgent = (String) session.getAttribute(USER_AGENT_ATTR);

        if (sessionUserAgent != null && !currentUserAgent.equals(sessionUserAgent)) {
            session.invalidate();
            return false;
        }

        return true;
    }

    // 로그아웃 시 세션 정리
    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    // 세션에서 사용자 ID 가져오기
    public String getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(USER_ID_ATTR);
        }
        return null;
    }
}