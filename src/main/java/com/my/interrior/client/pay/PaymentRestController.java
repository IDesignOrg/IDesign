package com.my.interrior.client.pay;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.client.ordered.OrderedService;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.common.CommonResponse;
import com.my.interrior.common.DefaultApiResponse;
import com.siot.IamportRestClient.exception.IamportResponseException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Payment")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class PaymentRestController {

	private final PaymentService paymentService;
	private final ShipmentService shipmentService;
	private final OrderedService orderedService;
	private final UserRepository userRepository;
	private final PaymentAndUserRepository paymentAndUserRepository;
	private final PaymentAndUserService PaymentAndUserService;
	// 결제 사전 검증
	@DefaultApiResponse
	@Operation(summary = "결제 사전 검증", description = "스크립트 공격 사전 방지")
	@PostMapping("/payment/prepare")
	public ResponseEntity<?> preparePayment(
			@Parameter(name = "request")
			@ModelAttribute PrePaymentEntity request,
			@RequestParam("validation") int validation) throws IamportResponseException, IOException {

		BigDecimal amount = (BigDecimal) request.getAmount();
		BigDecimal validate = BigDecimal.valueOf(validation);

		if (amount.compareTo(validate) == 0) {

			request.setAmount(amount);

			paymentService.postPrepare(request);

			return ResponseEntity.ok(CommonResponse.success("success"));
		} else {
			throw new NoSuchElementException("값이 맞지 않습니다.");
		}

	}

	// 결제 정보 저장
	@DefaultApiResponse
	@Operation(summary = "결제 정보 저장")
	@PostMapping("/save/payment")
	public ResponseEntity<PayEntity> savePayment(
			@Parameter(name = "pay", description = "결제 정보들 Payment 테이블에 저장")
			@RequestBody PayEntity pay, HttpSession session) throws Exception {

		if(pay == null)
			throw new IllegalArgumentException("결제 정보가 없습니다.");
		
		pay.setPaidAt(LocalDate.now());

		paymentService.saveMyPayment(pay);
		
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
	@DefaultApiResponse
	@Operation(summary = "환불 정보 저장")
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
	@DefaultApiResponse
	@Operation(summary = "배송 정보 저장")
	@PostMapping("/save/shipment")
	public ResponseEntity<ShipmentEntity> saveShipment(
			@Parameter(name = "shipment", description = "배송 정보")
			@RequestBody ShipmentEntity ship) throws Exception {
		
		if(ship == null)
			throw new IllegalArgumentException("배송 정보가 없습니다.");
		
		shipmentService.saveMyShipment(ship);

		return ResponseEntity.ok(ship);
	}

	// 부득이하게 URL 쿼리 문자열이 너무 길어져서 Post를 사용함.
	@DefaultApiResponse
	@Operation(summary = "주문 정보 확인", description = "쿼리 스트링이 너무 길어져서 POST 사용")
	@PostMapping("/payment/info")
	public ResponseEntity<?> goToPaymentInfo(
			@Parameter(name = "dto", description = "payment와 shipment의 정보들이 담겨있음.")
			@RequestBody PaymentAndShipmentDTO dto, HttpSession session)
			throws Exception {
		PaymentAndShipmentDTO.PaymentInfo paymentInfo = dto.getPaymentInfo();
		PaymentAndShipmentDTO.ShipmentInfo shipmentInfo = dto.getShipmentInfo();

		System.out.println("paymentInfo: " + paymentInfo);
		System.out.println("shipmentInfo: " + shipmentInfo);

		//paymentInfo에 있는 name과 paidAmount의 값을 테이블에 담아두자?
		
		session.setAttribute("paymentRes", paymentInfo);
		session.setAttribute("shipmentRes", shipmentInfo);

		return ResponseEntity.ok(CommonResponse.success("success"));
	}

}
