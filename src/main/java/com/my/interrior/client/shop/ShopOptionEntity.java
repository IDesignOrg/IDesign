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

@Entity(name="shop_option")
@Getter
@Setter
public class ShopOptionEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shopOptionNo;
	
	@Column(name="shop_option_name")
	private String shopOptionName;

	
	@Column(name="shop_option_value")
	private String shopOptionValue;
	
	 @Column(name="shop_option_stock")
	 private int shopOptionStock;
	
	@ManyToOne
	@JoinColumn(name="shop_no")
	private ShopEntity shopEntity;
}
