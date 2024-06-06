package com.my.interrior.client.mypage;

import com.my.interrior.client.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class MypageRestController {

    @Autowired
    private MypageService mypageService;

    @Autowired
    private   MypageRepository mypageRepository;


    @PutMapping("/mypage/{id}")
    public ResponseEntity<UserEntity> updateUserInfo(@RequestBody UserEntity userEntity,@PathVariable("Uid") String id) {

        System.out.println("userentity:" +userEntity);

        mypageRepository.save(userEntity);

        UserEntity user =  mypageService.getUserInfo(id);

        System.out.println("user2323:" +user);

        return ResponseEntity.ok(user);
    }

}
