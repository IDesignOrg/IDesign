package com.my.interrior.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.my.interrior.admin.chat.Chat;
import com.my.interrior.client.chat.BroadSocket;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	private final BroadSocket broadSocket;
    private final Chat chat;

    @Autowired
    public WebSocketConfig(BroadSocket broadSocket, Chat chat) {
        this.broadSocket = broadSocket;
        this.chat = chat;
    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	registry.addHandler(chat, "/adminBroadsocket").setAllowedOrigins("*");;
        // "/broadsocket" 엔드포인트에 BroadSocket 핸들러를 등록
        registry.addHandler(broadSocket, "/broadsocket").setAllowedOrigins("*");;
     // "/chat" 엔드포인트에 Chat 핸들러를 등록
    }
}
