package com.my.interrior.client.mypage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.interrior.client.user.UserEntity;

import jakarta.transaction.Transactional;



@Repository
public interface MypageRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUId(String UId);
    
    @Transactional
    void deleteByUId(String UId);
}
