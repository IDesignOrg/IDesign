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
	//기본값 1씩증가 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shopNo;
	//상품 이름
	@Column(nullable = false, name = "shop_title")
    private String shopTitle;
	//상품 내용(글자설명)
	@Column(nullable = false, columnDefinition = "TEXT", name = "shop_content")
    private String shopContent;
	//상품의 섬네일사진(1개만)
	@Column(nullable = false, name = "shop_main_photo")
    private String shopMainPhoto;
	//삼품의 가격
	@Column(nullable = false, name = "shop_price")
    private String shopPrice;
	//상품 카테고리 (드롭다운 박스로 가구,디퓨저,벽지,타일)
	@Column(nullable = false, name = "shop_category")
    private String shopCategory;
	//상품 할인률(퍼센트 1~100%)
	@Column(nullable = false, name = "shop_discount")
    private String shopDiscont;
	//상품의 조회수 값을 넣을필요없음
	@Column(nullable = false, name = "shop_hit")
    private int shopHit;
	//상품 판매수 값 넣을필요없음
	@Column(nullable = false, name = "shop_sell")
    private int shopSell;
	// 상품 작성 시간
	@Column(name = "shop_write_time", nullable = false)
	private LocalDateTime ShopWriteTime;
	//상품 활성화 비활성화
    @Column(nullable = false, name = "s_deactivated")
    private boolean SDeactivated = false;
    //상품 환불 값
    @Column(nullable = false, name = "shop_refund_count")
    private int shopRefundCount = 0;
}
