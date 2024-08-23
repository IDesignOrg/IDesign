package com.my.interrior.client.mypage;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class MypageController {

    @Autowired
    private UserRepository userRepository; // JpaRepository(save, deleteBy)

    @Autowired
    private MypageService mypageService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/mypage/mypage")
    public String myPage(HttpSession session, Model model) {

        String  UId = (String) session.getAttribute("UId");

        UserEntity user = mypageService.getUserInfo(UId);

        model.addAttribute("userInfo",user);


        return "client/mypage/mypage";
    }
    @GetMapping("mypage/check_password")
    public String getMethodName() {
        return "client/mypage/check_password";
    }
    @PostMapping("mypage/check_password")
    public String check_password(@RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
    	String UId = (String) session.getAttribute("UId");
        UserEntity user = mypageService.getUserInfo(UId);

        if (user != null && passwordEncoder.matches(password, user.getUPw())) {
            return "redirect:/mypage/mypageUpdate";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid password");
            return "redirect:/mypage/check-password";
        }
    }
    @GetMapping("mypage/mypageUpdate")
    public String mypageUpdateG(HttpSession session, Model model) {
    	String  UId = (String) session.getAttribute("UId");
    	UserEntity user = mypageService.getUserInfo(UId);
    	model.addAttribute("userInfo",user);
    	return "client/mypage/mypageUpdate";
    }
    @PostMapping("/UpdateForUser")
    public String mypageUpdate( @RequestParam("file") MultipartFile file,
            @RequestParam("UName") String UName,
            @RequestParam("UBirth") String UBirth,
            @RequestParam("UMail") String UMail,
            @RequestParam("UTel") String UTel,
            HttpSession session, Model model) throws IOException {
    	
    	
    	mypageService.updateUser(file, UName, UBirth, UMail, UTel);
    	String  UId = (String) session.getAttribute("UId");
    	UserEntity user = mypageService.getUserInfo(UId);

        model.addAttribute("userInfo",user);
    	
    	return "client/mypage/mypage";
    }
    @GetMapping("/mypage/mypageUpdatePassword")
    public String mypageUpdatePassword() {
    	return "client/mypage/mypageUpdatePassword";
    }
    
    
    
    
    
  
}
