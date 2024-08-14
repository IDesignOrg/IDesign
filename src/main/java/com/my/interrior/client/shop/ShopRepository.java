package com.my.interrior.client.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Repository
public interface ShopRepository extends JpaRepository<ShopEntity, Long>{
	Page<ShopEntity> findBySDeactivatedFalse(Pageable pageable);
	List<ShopEntity> findByShopNoIn(List<Long> shopNos);
	List<ShopEntity> findByShopTitleContainingAndShopCategoryContaining(
	        String shopTitle, String shopCategory
	    );
	Optional<ShopEntity> findById(Long shopNo);
	ShopEntity findByShopNo(Long shopNo);
	Optional<ShopEntity> findTopByOrderByShopHitDesc();

}
