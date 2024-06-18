package com.my.interrior.client.chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.my.interrior.admin.chat.Chat;

@Component
public class BroadSocket extends TextWebSocketHandler {

    // 서버와 유저간의 접속을 key로 구분하기 위한 내부 클래스
    private static class User {
        WebSocketSession session;
        String key;
    }
    private final Chat chat;
    // 유저와 서버간의 접속 리스트
    private List<User> sessionUsers = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    public BroadSocket(@Lazy Chat chat) {
        this.chat = chat;
    }

    // Session으로 접속 리스트에서 User 클래스를 탐색
    private User getUser(WebSocketSession session) {
        return searchUser(x -> x.session == session);
    }

    // key로 접속 리스트에서 User 클래스를 탐색
    private User getUser(String key) {
        return searchUser(x -> x.key.equals(key));
    }

    // 접속 리스트 탐색 함수
    private User searchUser(SearchExpression func) {
        Optional<User> op = sessionUsers.stream().filter(x -> func.expression(x)).findFirst();
        return op.orElse(null);
    }

    // WebSocket 연결이 열릴 때 실행됨
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 인라인 클래스 User를 생성
        User user = new User();
        // Unique 키를 발급("-"는 제거)
        user.key = UUID.randomUUID().toString().replace("-", "");
        // WebSocket의 세션
        user.session = session;
        // 유저 리스트에 등록
        sessionUsers.add(user);
        // 관리자한테 알리기
        chat.visit(user.key); // Chat 클래스의 visit 메서드 호출
    }

    // WebSocket으로 메시지가 왔을 때 실행됨
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Session으로 접속 리스트에서 User 클래스를 탐색하기
        User user = getUser(session);
        System.out.println("클라이언트에서 관리자로 보내는 메시지는? : " + message);
        
        // 접속 리스트에 User가 없으면
        if (user == null) {
            chat.bye(user.key); // Chat 클래스의 bye 메서드 호출
            sessionUsers.remove(user);
        }else {
        	//여기에 클라이언트에서 관리자로 메시지 보내는 로직 작성
        	chat.sendMessage(user.key, message.getPayload());
        }
    }

    // WebSocket 연결이 닫힐 때 실행됨
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        User user = getUser(session);
        if (user != null) {
            sessionUsers.remove(user);
            chat.bye(user.key); // Chat 클래스의 bye 메서드 호출
        }
    }

    // 관리자에게 메시지 보내는 함수
    public void sendMessage(String key, String message) {
        // key로 접속 리스트에서 User 클래스를 탐색
    	System.out.println("관리자에서 클라이언트로 보내는 메시지: " + message);
        User user = getUser(key);
        if (user != null) {
            try {
                user.session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 모든 유저의 key를 가져와서 List로 반환
    public List<String> visitAllUsers() {
        List<String> keys = new ArrayList<>();
        sessionUsers.forEach(user -> {
            keys.add(user.key);
        });
        return keys;
    }

    // 내부 인터페이스: 접속 리스트 필터링을 위한 표현식 인터페이스
    private interface SearchExpression {
        boolean expression(User user);
    }

}
