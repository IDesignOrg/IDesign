package com.my.interrior.client.shop;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name="shop_option")
public class ShopOption {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shopOptionNo;
	
	@Column(name = "shop_category")
	private String category;
	
	@Column(name="shop_option_first")
	private String shopOptionFirst;
	
	@Column(name ="shop_option_second")
	private String shopOptionSecond;
	
	@Column(name="shop_option_third")
	private String shopOptionThird;
	
	@ManyToOne
	@JoinColumn(name="shop_no")
	private ShopEntity shopEntity;
}
