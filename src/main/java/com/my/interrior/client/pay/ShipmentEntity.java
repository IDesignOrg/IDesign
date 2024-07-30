package com.my.interrior.client.pay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "shipment")
@Getter
@Setter
@ToString
public class ShipmentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shipmentNo;
	
	private String buyerName;
	private String buyerEmail;
	private String buyerAddr;
	private String buyerPostcode;
	private String buyerTel;
	@Column(columnDefinition = "TEXT")
	private String message; 
	
}
