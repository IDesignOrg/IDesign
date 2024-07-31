package com.my.interrior.client.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long>{
	List<ShopEntity> findByShopNoIn(List<Long> shopNos);
	List<ShopEntity> findByShopTitleContainingAndShopCategoryContaining(
	        String shopTitle, String shopCategory
	    );
}
