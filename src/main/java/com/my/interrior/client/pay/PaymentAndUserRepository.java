package com.my.interrior.client.pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAndUserRepository extends JpaRepository<PaymentAndUserEntity, Long>{
	void deleteByPayEntity_PayNo(Long payNo);
}
