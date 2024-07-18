package com.my.interrior.client.shop;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "shop")
public class ShopEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shopNo;
	
	@Column(nullable = false, name = "shop_title")
    private String shopTitle;
	
	@Column(nullable = false, columnDefinition = "TEXT", name = "shop_content")
    private String shopContent;
	
	@Column(nullable = false, name = "shop_main_photo")
    private String shopMainPhoto;
	
	@Column(nullable = false, name = "shop_price")
    private String shopPrice;
	
	@Column(nullable = false, name = "shop_category")
    private String shopCategory;
	
	@Column(nullable = false, name = "shop_stock")
    private String shopStock;
	
	@Column(nullable = false, name = "shop_discount")
    private String shopDiscont;
	
	@Column(nullable = false, name = "shop_hit")
    private String shopHit;
	
	@Column(nullable = false, name = "shop_sell")
    private String shopSell;
	
	@Column(name = "shop_write_time", nullable = false)
	private LocalDateTime ShopWriteTime;
}
