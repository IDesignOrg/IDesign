package com.my.interrior.client.pay;

import java.io.IOException;
import java.math.BigDecimal;


import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.client.ordered.OrderedService;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.siot.IamportRestClient.exception.IamportResponseException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentRestController {

	private final PaymentService paymentService;
	private final ShipmentService shipmentService;
	private final OrderedService orderedService;
	private final UserRepository userRepository;
	private final PaymentAndUserRepository paymentAndUserRepository;
	private final PaymentAndUserService PaymentAndUserService;
	// 결제 사전 검증
	@PostMapping("/payment/prepare")
	public ResponseEntity<?> preparePayment(@ModelAttribute PrePaymentEntity request,
			@RequestParam("validation") int validation) throws IamportResponseException, IOException {

		BigDecimal amount = (BigDecimal) request.getAmount();
		BigDecimal validate = BigDecimal.valueOf(validation);
		System.out.println("amount의 값은: " + amount);
		System.out.println("validate의 값은: " + validate);
		if (amount.compareTo(validate) == 0) {

			request.setAmount(amount);

			paymentService.postPrepare(request);

			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	// 결제 정보 저장
	// 어떤 유저가 결제했는 지도 저장하자
	@PostMapping("/save/payment")
	public ResponseEntity<PayEntity> savePayment(@RequestBody PayEntity pay, HttpSession session) throws Exception {
		System.out.println("pay.getPaidAt(): " + pay.getPaidAt());
		pay.setPaidAt(LocalDate.now());

		paymentService.saveMyPayment(pay);
		System.out.println("merchantUIdInPayment: " + pay.getMerchantUId());
		session.setAttribute("merchantUId", pay.getMerchantUId());

		String userId = (String) session.getAttribute("UId");
		// u_no, pay_no 저장하기
		UserEntity user = userRepository.findByUId(userId);

		PaymentAndUserEntity pau = new PaymentAndUserEntity();
		pau.setPayEntity(pay);
		pau.setUserEntity(user);

		paymentAndUserRepository.save(pau);

		return ResponseEntity.ok(pay);
	}

	// 환불
	@PostMapping("/refund/payment")
	public ResponseEntity<?> refundPayment(
			@RequestParam("merchantUId") String merchantUId,
			@RequestParam("refundReason") String refundReason,
			HttpSession session) throws Exception{
		String userid = (String)session.getAttribute("UId");
		String token = paymentService.getAccessToken();
		log.info("token값에는 : {}", token);
		paymentService.refundRequest(token, merchantUId);
		
		//여기에 주문 내역에서 삭제해야 됨.
		//ordered랑 payment랑 payment_user_mapping 테이블 전부인데 payment_user_mapping부터 지워야 됨.
		//shipment는 나중에 시간나면 추가해줘야 됨. 지금 shipment 지울만한 속성이 없음.
		orderedService.updateOrderedState(merchantUId, refundReason, userid);
		PayEntity pay = paymentService.findPayEntity(merchantUId);
		Long payNo = pay.getPayNo();
		//payment_user_mapping 제거
		PaymentAndUserService.deleteByPayNo(payNo);
		//payment 제거
		paymentService.deleteByMerchantUId(merchantUId);
		return ResponseEntity.ok().build();
	}
	@PostMapping("/save/shipment")
	public ResponseEntity<ShipmentEntity> saveShipment(@RequestBody ShipmentEntity ship) throws Exception {
		shipmentService.saveMyShipment(ship);

		return ResponseEntity.ok(ship);
	}

	// 부득이하게 URL 쿼리 문자열이 너무 길어져서 Post를 사용함.
	@PostMapping("/payment/info")
	public ResponseEntity<?> goToPaymentInfo(@RequestBody PaymentAndShipmentDTO dto, HttpSession session)
			throws Exception {
		PaymentAndShipmentDTO.PaymentInfo paymentInfo = dto.getPaymentInfo();
		PaymentAndShipmentDTO.ShipmentInfo shipmentInfo = dto.getShipmentInfo();

		System.out.println("paymentInfo: " + paymentInfo);
		System.out.println("shipmentInfo: " + shipmentInfo);

		//paymentInfo에 있는 name과 paidAmount의 값을 테이블에 담아두자?
		
		session.setAttribute("paymentRes", paymentInfo);
		session.setAttribute("shipmentRes", shipmentInfo);

		return ResponseEntity.ok("success");
	}

}
