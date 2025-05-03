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

    // �α��� ���� �� ���� ����
    public void createUserSession(HttpServletRequest request, String userId) {
        // ���� ������ �ִٸ� ��ȿȭ
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // �� ���� ����
        HttpSession newSession = request.getSession(true);

        // ����� ���� ����
        newSession.setAttribute(USER_ID_ATTR, userId);

        // ���� ������ ���� �߰� ���� ����
        newSession.setAttribute(USER_AGENT_ATTR, request.getHeader("User-Agent"));
        newSession.setAttribute(LOGIN_IP_ATTR, request.getRemoteAddr());
        newSession.setAttribute(LOGIN_TIME_ATTR, LocalDateTime.now());

    }

    // ���� ��ȿ�� ����
    public boolean validateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(USER_ID_ATTR) == null) {
            return false;
        }

        // �߰� ���� ����: User-Agent�� ����Ǿ����� Ȯ��
        String currentUserAgent = request.getHeader("User-Agent");
        String sessionUserAgent = (String) session.getAttribute(USER_AGENT_ATTR);

        if (sessionUserAgent != null && !currentUserAgent.equals(sessionUserAgent)) {
            session.invalidate();
            return false;
        }

        return true;
    }

    // �α׾ƿ� �� ���� ����
    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    // ���ǿ��� ����� ID ��������
    public String getUserIdFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(USER_ID_ATTR);
        }
        return null;
    }
}