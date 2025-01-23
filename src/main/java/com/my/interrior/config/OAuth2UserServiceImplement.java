package com.my.interrior.config;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.google.rpc.context.AttributeContext.Response;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.common.OAuthLoginService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {

	@Autowired
	private HttpServletRequest session;

	private final OAuthLoginService oAuthLoginService;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(request); // 기본 사용자 정보 로드
		String platform = request.getClientRegistration().getClientName().toLowerCase(); // 플랫폼 이름
		UserEntity userEntity = null;

		try {
			// 사용자 정보와 플랫폼 이름을 보내 사용자 정보를 가져옴
			userEntity = oAuthLoginService.SNSLogin(oAuth2User, platform);
			// 로그인한 ID를 세션에 저장해서 홈페이지 기능 이용 가능
			session.getSession().setAttribute("UId", userEntity.getUId());
			System.out.println("성공");
		} catch (

		Exception e) {
			OAuth2Error error = new OAuth2Error("invalid_token", "Failed to process OAuth2 login", null);
			throw new OAuth2AuthenticationException(error, e.getMessage());
		}

		return new CustomOAuth2User(userEntity, oAuth2User.getAttributes());
	}

}
