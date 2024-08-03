package com.my.interrior.client.pay;

import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "payment_user_mapping")
@Getter
@Setter
@ToString
public class PaymentAndUserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentUserNo;
	
	@JoinColumn(name = "u_no")
	@ManyToOne
	private UserEntity userEntity;
	
	@JoinColumn(name = "pay_no")
	@ManyToOne
	private PayEntity payEntity;
}
