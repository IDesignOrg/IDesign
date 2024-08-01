package com.my.interrior.client.cart;

import com.my.interrior.client.shop.ShopOptionValueEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name="cart_option")
@Getter
@Setter
public class CartOptionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="co_no")
	private Long CoNo;
	
	@ManyToOne
	@JoinColumn(name = "c_no")
	private CartEntity cartEntity;
	
	@ManyToOne
	@JoinColumn(name = "shop_option_value_no")
	private ShopOptionValueEntity shopOptionValueEntity;
}
