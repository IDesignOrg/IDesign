package com.my.interrior.client.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
//save뺴고는 여기다가 다 박아라

	UserEntity findByUIdAndUPw(String UId, String UPw);

	UserEntity findByUId(String UId);

	UserEntity findByUMail(String email);

	UserEntity findByUPwAndUMail(String UPw, String UMail);

	UserEntity findByUMailAndUName(String UMail, String UName);

	UserEntity findByUNo(Long UNo);

	List<UserEntity> findByUNameContainingIgnoreCaseAndURegisterBetween(String uName, LocalDate startDate, LocalDate endDate);

    List<UserEntity> findByUIdContainingIgnoreCaseAndURegisterBetween(String uId, LocalDate startDate, LocalDate endDate);

    List<UserEntity> findByUMailContainingIgnoreCaseAndURegisterBetween(String uMail, LocalDate startDate, LocalDate endDate);

    List<UserEntity> findAllByURegisterBetween(LocalDate startDate, LocalDate endDate);

}
