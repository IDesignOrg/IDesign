package com.my.interrior.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.my.interrior.client.user.UserEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserEntity userEntity;  // DB의 사용자 정보
    private final Map<String, Object> attributes; // 원본 OAuth2 데이터

    public CustomOAuth2User(UserEntity userEntity, Map<String, Object> attributes) {
        this.userEntity = userEntity;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes; // 원본 데이터를 반환
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_USER");
    }

    @Override
    public String getName() {
        return userEntity != null ? userEntity.getUId() : "Unknown";
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }
}
