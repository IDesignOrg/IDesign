package com.my.interrior.client.pay;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.my.interrior.admin.coupon.CouponService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PaymentController {

	private final CouponService couponService;
	
	@GetMapping("/payment/info")
	public String goToInfo(Model model, HttpSession session) {

		PaymentAndShipmentDTO.PaymentInfo paymentInfo = (PaymentAndShipmentDTO.PaymentInfo) session
				.getAttribute("paymentRes");
		PaymentAndShipmentDTO.ShipmentInfo shipmentInfo = (PaymentAndShipmentDTO.ShipmentInfo) session
				.getAttribute("shipmentRes");

		
		System.out.println("paymentInfo: get에서 시작함" + paymentInfo);
		System.out.println("shipmentInfo: get에서 시작함" + shipmentInfo);
		
		if (paymentInfo != null && shipmentInfo != null) {
			System.out.println("둘 다 있어서 여기 안까지 들어옴");
			model.addAttribute("paymentRes", paymentInfo);
			model.addAttribute("shipmentRes", shipmentInfo);

			// 세션에서 데이터 제거
			session.removeAttribute("paymentRes");
			session.removeAttribute("shipmentRes");
		}
		return "client/pay/paymentInfo";
	}
}
