package com.my.interrior.common;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

@Service
public class OAuthLoginService {

	@Autowired
	private UserRepository userRepository;

	public UserEntity SNSLogin(OAuth2User oAuth2User, String platform) {
		// 받아온 데이터를 플랫폼에 따라 가공
		Map<String, Object> standardizedAttributes = extractAttributes(oAuth2User.getAttributes(), platform);
		// 가공된 데이터에서 사용자 ID 추출
		String userId = (String) standardizedAttributes.get("userId");
		// 데이터베이스에서 사용자 조회
		UserEntity userEntity = userRepository.findByUId(userId);
		// 사용자가 없을시 회원가입 진행
		if (userEntity == null) {
			userEntity = new UserEntity();
			userEntity.setUId(userId);
			userEntity.setUMail((String) standardizedAttributes.getOrDefault("email", ""));
            userEntity.setUName((String) standardizedAttributes.getOrDefault("nickname", "Unknown"));
            userEntity.setUPofile((String) standardizedAttributes.getOrDefault("profile_image", ""));
            userEntity.setURegister(LocalDate.now());
            //값을 가져오지 못하는거는 빈문자열로 저장
            userEntity.setUBirth("");
            userEntity.setUPw("");
            userEntity.setUTel("");
            //데이터 베이스에 저장
            userRepository.save(userEntity);
		}
		// 최종 사용자 반환
		return userEntity;
	}
	
	
	//받아온 데이터를 사용할수 있게 가공
	@SuppressWarnings("unchecked")
	private Map<String, Object> extractAttributes(Map<String, Object> attributes, String platform) {
	    Map<String, Object> standardizedAttributes = new HashMap<>();

	    if ("kakao".equals(platform)) {
	    	//카카오 계정 정보 추출
	        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
	        Map<String, Object> profile = kakaoAccount != null ? (Map<String, Object>) kakaoAccount.get("profile") : null;

	        //표준화된 데이터 맵에 추가
	        standardizedAttributes.put("email", kakaoAccount != null ? kakaoAccount.get("email") : "");
	        standardizedAttributes.put("nickname", profile != null ? profile.get("nickname") : "Unknown");
	        standardizedAttributes.put("profile_image", profile != null ? profile.get("profile_image_url") : "");
	        standardizedAttributes.put("userId", platform.toLowerCase() + "_" + attributes.get("id")); // 사용자 ID 추가
	    } else if ("naver".equals(platform)) {
	    	//네이버 계정 정보 추출
	        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
	        
	        standardizedAttributes.put("email", response != null ? response.get("email") : "");
	        standardizedAttributes.put("nickname", response != null ? response.get("nickname") : "Unknown");
	        standardizedAttributes.put("profile_image", response != null ? response.get("profile_image") : "");
	        standardizedAttributes.put("userId", platform.toLowerCase() + "_" + (response != null ? response.get("id") : "")); // 사용자 ID 추가
	    } else if ("google".equals(platform)) {
	    	//구글 oaut응답 전체 확인
	    	System.out.println("[Google 응답 전체 확인 ] : " + attributes);
	    	//구글 계정 정보 추출
	        standardizedAttributes.put("email", attributes.get("email"));
	        standardizedAttributes.put("nickname", attributes.get("name"));
	        standardizedAttributes.put("profile_image", attributes.get("picture"));
	        standardizedAttributes.put("userId", platform.toLowerCase() + "_" + attributes.get("sub")); // 사용자 ID 추가
	        
	        //각 속성별로 값들 확인
	        System.out.println(" 이메일: " + attributes.get("email"));
	        System.out.println(" 이름(name): " + attributes.get("name"));
	        System.out.println(" 프로필 이미지: " + attributes.get("picture"));
	        System.out.println(" 고유 ID(sub): " + attributes.get("sub"));
	    } else {
	        throw new IllegalArgumentException("지원되지 않는 플랫폼: " + platform);
	    }

	    return standardizedAttributes;
	}

		
}
