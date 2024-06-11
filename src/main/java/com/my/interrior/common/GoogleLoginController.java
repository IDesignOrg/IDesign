package com.my.interrior.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@CrossOrigin("*")
//로컬 환경에서 CORS 에러가 발생하여 선언함.
public class GoogleLoginController {

	@Autowired
	private GoogleApi googleApi;
	
	//토큰, 사용자 정보 가져오기
	@GetMapping(value="/auth/login/google")
    public String loginGoogle(@RequestParam(value = "code") String authCode){
        RestTemplate restTemplate = new RestTemplate();
        
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleApi.getClientId())
                .clientSecret(googleApi.getClientSecret())
                .code(authCode)
                .redirectUri("http://localhost:8080/auth/login/google")
                .grantType("authorization_code").build();
        
        ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                googleOAuthRequestParam, GoogleResponse.class);
        
        String jwtToken=resultEntity.getBody().getId_token();
        
        Map<String, String> map=new HashMap<>();
        
        map.put("id_token",jwtToken);
        
        ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
                map, GoogleInfResponse.class);
        
        System.out.println("resultEntity2의 값은? : " + resultEntity2);
        
        String email=resultEntity2.getBody().getEmail(); 
        
        System.out.println("내 이메일은 : " + email);
        //이메일을 통해 비교 후 데이터베이스의 이메일과 일치하면 로그인을, 아니면 회원가입으로 이동
        
        return email;
    }
	
	
}
