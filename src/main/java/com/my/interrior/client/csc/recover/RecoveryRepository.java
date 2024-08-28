package com.my.interrior.client.csc.recover;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecoveryRepository extends JpaRepository<RecoveryEntity, Long>{
	List<RecoveryEntity> findByUser_UIdContainingIgnoreCaseAndRequestDateBetween(String uId, LocalDate startDate, LocalDate endDate);

    List<RecoveryEntity> findByUser_UNameContainingIgnoreCaseAndRequestDateBetween(String uName, LocalDate startDate, LocalDate endDate);

    List<RecoveryEntity> findByUser_UMailContainingIgnoreCaseAndRequestDateBetween(String uMail, LocalDate startDate, LocalDate endDate);
}
