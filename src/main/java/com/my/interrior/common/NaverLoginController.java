package com.my.interrior.common;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.client.user.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class NaverLoginController {

	@Autowired
	private NaverApi naverApi;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userrepository;
	
	private static final String TOKEN_REQUEST_URL = "https://nid.naver.com/oauth2.0/token";

	private static final String PROFILE_REQUEST_URL = "https://openapi.naver.com/v1/nid/me";

	@RequestMapping("/auth/login/naver")
	public String naverCallback(@RequestParam("code") String code, @RequestParam("code") String state, HttpSession session) throws JsonProcessingException {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", naverApi.getClientId());
		params.add("client_secret", naverApi.getClientSecret());
		params.add("code", code);
		params.add("state", state);
		// Parameter로 전달할 속성들 추가

		HttpEntity<MultiValueMap<String, String>> naverTokenRequest = makeTokenRequest(params);
		System.out.println("naverTokenRequest의 값들: " + naverTokenRequest);
		// Http 메시지 생성

		RestTemplate rt = new RestTemplate();
		ResponseEntity<String> tokenResponse = rt.exchange(TOKEN_REQUEST_URL, HttpMethod.POST, naverTokenRequest,
				String.class);
		System.out.println("tokenResponse의 값은: " + tokenResponse);
		// TOKEN_REQUEST_URL로 Http 요청 전송

		ObjectMapper objectMapper = new ObjectMapper();
		NaverOAuthToken naverToken = objectMapper.readValue(tokenResponse.getBody(), NaverOAuthToken.class);
		System.out.println("naverToken의 값듣은 : " + naverToken);
		// ObjectMapper를 통해 NaverOAuthToken 객체로 매핑

		HttpEntity<MultiValueMap<String, String>> naverProfileRequest = makeProfileRequest(naverToken);

		ResponseEntity<String> profileResponse = rt.exchange(PROFILE_REQUEST_URL, HttpMethod.POST, naverProfileRequest,
				String.class);

		NaverProfileResponse naverProfileResponse = objectMapper.readValue(profileResponse.getBody(),
				NaverProfileResponse.class);
		System.out.println("naverProfileResponse의 값들: " + naverProfileResponse);
	
		String email = naverProfileResponse.getResponse().getEmail();
		System.out.println("user의 email은: " + email);
		String naverId = naverProfileResponse.getResponse().getId();
		
		
		UserEntity existingUser = userrepository.findByUId(naverId);
		System.out.println("아이디 확인 : " + existingUser);
		
		if(existingUser != null) {
			session.setAttribute("UId", naverId);
			System.out.println("네이버 세션 등록 : " + naverId);
			return "redirect:/";
		}else {
			UserEntity newUser = new UserEntity();
            newUser.setUMail(email);
            newUser.setUName("naver-" + naverProfileResponse.getResponse().getNickname());
            newUser.setURegister(LocalDate.now());
            newUser.setUPw(""); // 비밀번호는 네이버 로그인에서는 사용되지 않으므로 빈 값
            newUser.setUPofile(naverProfileResponse.getResponse().getProfile_image());
            newUser.setUBirth(naverProfileResponse.getResponse().getBirthyear() + "-" + naverProfileResponse.getResponse().getBirthday());
            newUser.setUTel(naverProfileResponse.getResponse().getMobile());
            newUser.setUId(naverProfileResponse.getResponse().getId());
            userrepository.save(newUser);

            session.setAttribute("UId", newUser.getUId());
            return "redirect:/";
		}
		
		
	}

	private HttpEntity<MultiValueMap<String, String>> makeTokenRequest(MultiValueMap<String, String> params) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);
		return naverTokenRequest;
	}

	private HttpEntity<MultiValueMap<String, String>> makeProfileRequest(NaverOAuthToken naverToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + naverToken.getAccess_token());
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		HttpEntity<MultiValueMap<String, String>> naverProfileRequest = new HttpEntity<>(headers);
		return naverProfileRequest;
	}

}
