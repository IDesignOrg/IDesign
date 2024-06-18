package com.my.interrior.admin.chat;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.my.interrior.client.chat.BroadSocket;

@Component
public class Chat extends TextWebSocketHandler {

    private WebSocketSession adminSession = null;

    private final BroadSocket broadSocket;

    @Autowired
    public Chat(@Lazy BroadSocket broadSocket) {
        this.broadSocket = broadSocket;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    		System.out.println("session의 값은: " + session);
    		System.out.println("adminSession의 값은: " + adminSession);
        if (adminSession == null) { // 기존에 adminSession이 없을 때만 새로운 세션을 할당
            adminSession = session;
            // 기존에 접속해 있는 유저의 정보를 관리자에게 보냅니다.
            for (String key : broadSocket.visitAllUsers()) {
                visit(key);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            System.out.println("payload의 값들은: " + payload);
            String[] split = payload.split("#####", 2);
            if (split.length >= 2) {
                String key = split[0];
                String msg = split[1];
                broadSocket.sendMessage(key, msg);
            } else {
                System.err.println("Invalid message format: " + payload);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session == adminSession) {
            adminSession = null; // 연결이 닫힌 세션이 adminSession인 경우에만 null로 초기화
        }
    }

    // 관리자로 메시지를 보내는 함수
    public void sendToAdmin(String message) {
        if (adminSession != null && adminSession.isOpen()) {
            try {
                adminSession.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 일반 유저가 접속했을 때, 관리자에게 알려주기
    public void visit(String key) {
    	System.out.println("클라이언트가 visit함");
        sendToAdmin("{\"status\":\"visit\", \"key\":\"" + key + "\"}");
    }
    public void sendMessage(String key, String message) {
    	sendToAdmin("{\"status\":\"message\", \"key\":\"" + key + "\", \"message\":\"" + message + "\"}");
    }
    // 일반 유저가 접속을 끊을 때, 관리자에게 알리기
    public void bye(String key) {
        sendToAdmin("{\"status\":\"bye\", \"key\":\"" + key + "\"}");
    }
}
