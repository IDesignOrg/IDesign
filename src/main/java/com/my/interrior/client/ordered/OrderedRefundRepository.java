package com.my.interrior.client.ordered;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedRefundRepository extends JpaRepository<OrderedRefundEntity, Long>{
	Optional<OrderedRefundEntity> findByOrderedEntity_MerchantUId(String merchantUId);
}
