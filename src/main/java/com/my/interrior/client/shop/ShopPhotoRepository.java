package com.my.interrior.client.shop;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.my.interrior.client.evaluation.ReviewPhotoEntity;

public interface ShopPhotoRepository extends JpaRepository<ShopPhotoEntity, Long>{
	List<ShopPhotoEntity> findByshopEntity_shopNo(Long shopNo);
}
