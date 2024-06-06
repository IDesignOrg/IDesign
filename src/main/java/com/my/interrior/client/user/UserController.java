package com.my.interrior.client.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository; // JpaRepository(save, deleteBy)

    @Autowired
    private UserService userService;

    @GetMapping("/auth/login")
    public String LoginPage(){
        return "client/login";
    }

    @GetMapping("/auth/join")
    public String join(){
        return "client/join";
    }

    @PostMapping("/auth/join")
    public String join(@ModelAttribute UserEntity userEntity){
        userEntity.setURegister(LocalDate.now());
        //insert
        userRepository.save(userEntity);

        return "redirect:/auth/login"; // auth/login 경로로 리다이렉트
    }

    @PostMapping("/auth/login")
    public String login(@ModelAttribute UserDTO userDTO, HttpSession session , Model model){
        String UId = userDTO.getUId();
        String UPw = userDTO.getUPw();
        UserEntity userEntity = userService.checkLogin(UId, UPw);

        if (userEntity != null) {
            session.setAttribute("UId", userEntity.getUId());
            return "redirect:/";
        } else {
            model.addAttribute("loginError", "입력하신 정보를 확인하세요.");
            return "client/login";
        }
    }
}

