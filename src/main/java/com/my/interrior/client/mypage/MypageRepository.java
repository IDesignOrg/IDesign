package com.my.interrior.client.mypage;

import com.my.interrior.client.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MypageRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUId(String UId);
}
