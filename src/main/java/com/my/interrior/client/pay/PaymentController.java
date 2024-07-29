package com.my.interrior.client.pay;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siot.IamportRestClient.exception.IamportResponseException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	
	@PostMapping("/payment/prepare")
	public ResponseEntity<?> preparePayment(@ModelAttribute PrePaymentEntity request, @RequestParam("validation") int validation)
				throws IamportResponseException, IOException {
		
		BigDecimal amount = (BigDecimal) request.getAmount();
		BigDecimal validate = BigDecimal.valueOf(validation);
		System.out.println("amount의 값은: " + amount);
		System.out.println("validate의 값은: " + validate);
		if(amount.compareTo(validate) == 0) {
			
			request.setAmount(amount);
			
			paymentService.postPrepare(request);
			
			return ResponseEntity.ok("success");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		
	}
}
