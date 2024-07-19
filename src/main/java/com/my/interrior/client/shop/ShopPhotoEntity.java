package com.my.interrior.client.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name="shop_photo")
@Getter
@Setter
public class ShopPhotoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopPhotoNo;
	
	@Column(name = "shop_photo_url")
	private String shopPhotoUrl;
	
	@ManyToOne
	@JoinColumn(name = "shop_no")
	private ShopEntity shopEntity;
	
	
}
