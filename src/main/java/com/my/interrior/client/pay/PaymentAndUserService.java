package com.my.interrior.client.pay;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentAndUserService {

	private final PaymentAndUserRepository paymentAndUserRepository;
	
	@Transactional
	public void deleteByPayNo(Long payNo) {
		paymentAndUserRepository.deleteByPayEntity_PayNo(payNo);
	}
}
