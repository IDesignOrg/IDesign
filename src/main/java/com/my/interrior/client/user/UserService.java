package com.my.interrior.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String checkLogin(){

        return "";
    }

    public UserEntity checkLogin(String UId, String UPw) {

        UserEntity UserId = userRepository.findByUIdAndUPw(UId,UPw);

        return UserId;

    }
}
