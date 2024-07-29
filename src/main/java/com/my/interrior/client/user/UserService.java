package com.my.interrior.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.my.interrior.common.MailDTO;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
   
    public UserEntity checkLogin(String UId, String UPw) {

        UserEntity UserId = userRepository.findByUIdAndUPw(UId,UPw);

        return UserId;

    }
    
    public UserEntity checkLogin(String UId) {

        UserEntity UserId = userRepository.findByUId(UId);

        return UserId;

    }

    public UserEntity checkUserByEmail(String email) {
    	UserEntity user = userRepository.findByUMail(email);
    	
    	if(user != null) {
    		
    		return user;
    	}else {
    		return null;
    	}
    }
    public MailDTO checkMailAndName(String mail, String name) {
    	
    	String str = getTempPassword();
        MailDTO dto = new MailDTO();
        dto.setAddress(mail);
        dto.setTitle("아이디자인 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 아이디자인 임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
                + str + " 입니다." + "로그인 후에 마이페이지에서 비밀번호 변경을 해주세요");
        UserEntity user = userRepository.findByUMail(mail);
        user.setUPw(passwordEncoder.encode(str));
        
        userRepository.save(user);
        
        return dto;
    }
    
  //랜덤함수로 임시비밀번호 구문 만들기
    public String getTempPassword(){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        String str = "";

        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 6; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }
    // 메일보내기
    public void mailSend(MailDTO mailDTO) {
        System.out.println("전송 완료!");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDTO.getAddress());
        message.setSubject(mailDTO.getTitle());
        message.setText(mailDTO.getMessage());
        message.setFrom("dnlsemtmf@gmail.com");
        message.setReplyTo("dnlsemtmf@gmail.com");
        System.out.println("message"+message);
        mailSender.send(message);
    }
}
