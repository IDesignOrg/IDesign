package com.my.interrior.client.shop;

import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity(name = "shop_review_user_mapping")
public class ShopReviewUserMapping {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shopReivewUserNo;
	
	@ManyToOne
	@JoinColumn(name = "shop_no")
	private ShopEntity shopEntity;
	
	@ManyToOne
	@JoinColumn(name = "shop_review_no")
	private ShopReviewEntity shopReivewNoEntity;
	
	@ManyToOne
	@JoinColumn(name = "u_no")
	private UserEntity userEntity;
	
	
}
