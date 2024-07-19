package com.my.interrior.client.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopOptionRepository extends JpaRepository<ShopOptionEntity, Long>{
	List<ShopOptionEntity> findbyshopOption_shopNo(Long shopNo);
}
