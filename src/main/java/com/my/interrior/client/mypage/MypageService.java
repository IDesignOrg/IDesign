package com.my.interrior.client.mypage;

import com.my.interrior.client.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

    @Autowired
    private MypageRepository mypageRepository;

    public UserEntity getUserInfo(String UId) {

        return mypageRepository.findByUId(UId);
    }
    
    public void deleteUserInfo(String UId) {
    	mypageRepository.deleteByUId(UId);
    	
    } 

}
