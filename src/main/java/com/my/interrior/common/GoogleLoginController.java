package com.my.interrior.common;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.client.user.UserService;

import jakarta.servlet.http.HttpSession;
/*
@Controller
//@CrossOrigin("*")
//로컬 환경에서 CORS 에러가 발생하여 선언함.
public class GoogleLoginController {

	@Autowired
	private GoogleApi googleApi;
	
	@Autowired
	private UserService userService;
	
	@Autowired UserRepository userRepository;
	
	//토큰, 사용자 정보 가져오기
	@GetMapping(value="/auth/login/google")
    public String loginGoogle(@RequestParam(value = "code") String authCode, HttpSession session,Model model, RedirectAttributes redirectAttributes){
        RestTemplate restTemplate = new RestTemplate();
        
        GoogleRequest googleOAuthRequestParam = GoogleRequest
                .builder()
                .clientId(googleApi.getClientId())
                .clientSecret(googleApi.getClientSecret())
                .code(authCode)
                .redirectUri("https://idesign.r-e.kr/auth/login/google")
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
        String name = resultEntity2.getBody().getName();
        String profile = resultEntity2.getBody().getPicture();
        String uid = resultEntity2.getBody().getSub();
        System.out.println("이메일 값 : " +email);
        System.out.println("이름 값 : " +name);
        System.out.println("프로필 사진 값 : " +profile);
        System.out.println("아이디 값 : " + uid);

        
        System.out.println("내 이메일은 : " + email);
        //이메일을 통해 비교 후 데이터베이스의 이메일과 일치하면 로그인을, 아니면 회원가입으로 이동
        UserEntity existingUser = userRepository.findByUId("Google_"+uid);
        if(existingUser != null) {
        	if (existingUser.isUDeactivated()) {
        		//비활성화 계정일경우 비활성화아이디 복구 페이지로 이동 
        		redirectAttributes.addFlashAttribute("deactivatedError", "비활성화된 아이디입니다. 복구신청을 하시겠습니까?");
				redirectAttributes.addFlashAttribute("userNo", existingUser.getUNo());
                return "redirect:/auth/login";
            }
        	session.setAttribute("UId", existingUser.getUId());
        	
        	return "redirect:/";
        }else {
        	
        	UserEntity newUser = new UserEntity();
            newUser.setUMail(email);
            newUser.setUName("");
            newUser.setURegister(LocalDate.now());
            newUser.setUPw("");
            newUser.setUPofile("https://storage.googleapis.com/idesgins3/static/static_static_blank-profile-picture-973460_640.png");
            newUser.setUBirth("");
            newUser.setUTel("");
            newUser.setUId("Google_"+uid);
            userRepository.save(newUser); // 새로운 사용자 저장
            session.setAttribute("UId", newUser.getUId());
            
            
        	return "redirect:/";
        }
    }
	
	
}
*/
