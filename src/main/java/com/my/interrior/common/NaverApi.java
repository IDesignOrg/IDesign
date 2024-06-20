package com.my.interrior.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.naver")
public class NaverApi {
	
private String clientId;

private String clientSecret;

private String redirectUri;

private String authorizationGrantType;
}
