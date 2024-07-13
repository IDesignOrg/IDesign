package com.my.interrior.client.shop;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "shop_")
public class ShopReviewEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shopReviewNo;
	
	@Column(name = "shop_revice_title")
	private String shopReviewTitle;
	
	@Column(name = "shop_review_created")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate shopReviewCreated;
	
	@Column(columnDefinition = "TEXT" , name = "shop_review_content")
	private String shopReviceContent;
	
	@Column(name = "shop_review_star_rating")
	private double shopReviewStarRating;
	
	@Column(name = "shop_review_url")
	private String shopReviewUrl;
	
	@Column(name = "shop_review_author")
	private String shopReviewAuthor;
}
