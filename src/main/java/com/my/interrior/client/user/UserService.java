package com.my.interrior.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

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
    

}
