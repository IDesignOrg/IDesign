package com.my.interrior.client.pay;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentService {

	private IamportClient api;

	@Value("${imp.api.key}")
	private String apiKey;
	@Value("${imp.api.secretKey}")
	private String secretKey;
	// 테스트용
	static final String reason = "테스트용 환불처리";
	
	private final PrePaymentRepository prePaymentRepository;
	private final PaymentRepository paymentRepository;
	private final RestTemplate restTemplate;

	@PostConstruct
	public void init() {
		this.api = new IamportClient(apiKey, secretKey);
	}

	public void postPrepare(PrePaymentEntity request) throws IamportResponseException, IOException {
		PrepareData prepareData = new PrepareData(request.getMerchant_uid(), request.getAmount());

		api.postPrepare(prepareData); // 사전 등록 api

		prePaymentRepository.save(request);
	}

	public void saveMyPayment(PayEntity pay) throws Exception {
		paymentRepository.save(pay);
	}

	// 환불을 받기 위해 토큰 발급
	public String getAccessToken() {
		String url = "https://api.iamport.kr/users/getToken";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestJson = String.format("{\"imp_key\":\"%s\",\"imp_secret\":\"%s\"}", apiKey, secretKey);
		HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

		ResponseEntity<IamportTokenResponse> response = restTemplate.exchange(url, HttpMethod.POST, request,
				IamportTokenResponse.class);
		log.info("response에는 : {}", response);
		if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
			return response.getBody().getResponse().getAccess_token();
		} else {
			throw new RuntimeException("토큰 발급 실패");
		}

	}

	public void refundRequest(String access_token, String merchant_uid) throws IOException {
		String url = "https://api.iamport.kr/payments/cancel";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", access_token); // Authorization header에 token 설정

		String requestJson = String.format("{\"merchant_uid\":\"%s\", \"reason\":\"%s\"}", merchant_uid, reason);
		HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
		log.info("response에는 : {}", response);
		if (response.getStatusCode() == HttpStatus.OK) {
			log.info("결제 취소 완료 : 주문 번호 {}", merchant_uid);
		} else {
			log.error("결제 취소 실패 : 주문 번호 {}, 상태 코드 {}", merchant_uid, response.getStatusCode());
		}
	}

	public PayEntity findPayEntity(String merchantUId) {
		return paymentRepository.findByMerchantUId(merchantUId);
	}

	public void deleteByMerchantUId(String merchantUId) {
		paymentRepository.deleteByMerchantUId(merchantUId);
	}
}
