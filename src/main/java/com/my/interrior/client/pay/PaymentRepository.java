package com.my.interrior.client.pay;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PayEntity, Long>{

	PayEntity findByMerchantUId(String merchantUId);
	void deleteByMerchantUId(String merchantUId);
}
