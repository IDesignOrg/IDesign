package com.my.interrior.client.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository; // JpaRepository(save, deleteBy)

    @GetMapping("/auth/login")
    public String LoginPage(){
        return "client/login";
    }

    @GetMapping("/auth/join")
    public String join(){
        return "client/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute UserEntity userEntity){
        userEntity.setURegister(LocalDate.now());
        //insert
        userRepository.save(userEntity);

        return "client/login";
    }
}
