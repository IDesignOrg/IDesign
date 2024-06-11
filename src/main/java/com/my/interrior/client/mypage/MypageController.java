package com.my.interrior.client.mypage;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MypageController {

    @Autowired
    private UserRepository userRepository; // JpaRepository(save, deleteBy)

    @Autowired
    private MypageService mypageService;


    @GetMapping("/mypage")
    public String myPage(HttpSession session, Model model) {

        String  UId = (String) session.getAttribute("UId");

        UserEntity user = mypageService.getUserInfo(UId);

        model.addAttribute("userInfo",user);


        return "client/mypage";
    }
    
    
    
    
    
  
}
