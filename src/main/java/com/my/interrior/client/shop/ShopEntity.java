package com.my.interrior.client.shop;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "shop")
@Getter
@Setter
@ToString
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
	
	@Column(nullable = false, name = "shop_discount")
    private String shopDiscont;
	
	@Column(nullable = false, name = "shop_hit")
    private int shopHit;
	
	@Column(nullable = false, name = "shop_sell")
    private int shopSell;
	
	@Column(name = "shop_write_time", nullable = false)
	private LocalDateTime ShopWriteTime;
	
    @Column(nullable = false, name = "s_deactivated")
    private boolean SDeactivated = false;
    
    @Column(nullable = false, name = "shop_refund_count")
    private int shopRefundCount = 0;
}
