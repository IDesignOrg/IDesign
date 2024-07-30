package com.my.interrior.client.pay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentAndShipmentDTO {
	private PaymentInfo paymentInfo;
	private ShipmentInfo shipmentInfo;
	
	@Getter
	@Setter
	@ToString
	public static class PaymentInfo{
		private String merchantUId;
		private String cardName;
		private String name;
		private int paidAmount;
		private String payMethod;
		private String paidAt;
		private String status;
		private boolean success;
	}
	
	@Getter
	@Setter
	@ToString
	public static class ShipmentInfo{
		private String buyerName;
		private String buyerEmail;
		private String buyerAddr;
		private String buyerPostcode;
		private String buyerTel;
		private String message;
	}
}
