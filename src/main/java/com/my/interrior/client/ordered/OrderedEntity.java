package com.my.interrior.client.ordered;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "ordered")
@Setter
@Getter
@ToString
public class OrderedEntity {

	//여기에 payment_user_mapping의 pay_no가 필요함
	//pay_no로 merchant_uid 찾을 수 있음.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderedNo;
	private Long orderedNumber;
	private String orderedState;
	private String shipmentState;
	private LocalDate orderedDate;
	@Column(name = "shop_no")
	private Long shopNo;
	
	@JoinColumn(name = "u_no")
	@ManyToOne
	@JsonIgnore
	private UserEntity userEntity;
	
	@Column(name = "merchant_uid")
	private String merchantUId;
	@Column(name = "quantity")
	private int quantity;
}
