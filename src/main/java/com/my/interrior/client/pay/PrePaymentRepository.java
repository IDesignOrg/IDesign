package com.my.interrior.client.pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrePaymentRepository extends JpaRepository<PrePaymentEntity, Long>{

}
