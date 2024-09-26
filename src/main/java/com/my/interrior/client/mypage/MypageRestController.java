package com.my.interrior.client.mypage;

import com.my.interrior.client.user.UserEntity;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
public class MypageRestController {

    @Autowired
    private MypageService mypageService;

    @Autowired
    private   MypageRepository mypageRepository;


    @PutMapping("/mypage/{UId}")
    public ResponseEntity<UserEntity> updateUserInfo(@RequestBody UserEntity userEntity,@PathVariable("UId") String id) {
        System.out.println("userentity:" +userEntity);

        mypageRepository.save(userEntity);

        UserEntity user =  mypageService.getUserInfo(id);

        return ResponseEntity.ok(user);
    }
    
    
    @DeleteMapping("/delete/{UId}")
    public ResponseEntity<String> delete(@PathVariable("UId") String id) {
        
        mypageService.deleteUserInfo(id);
        
		return ResponseEntity.ok("success");


    	
    }   

}
