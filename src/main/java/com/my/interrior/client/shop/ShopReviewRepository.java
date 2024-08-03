package com.my.interrior.client.shop;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.interrior.client.user.UserEntity;

public interface ShopReviewRepository extends JpaRepository<ShopReviewEntity, Long>{
	int countByUser(UserEntity user);
}
