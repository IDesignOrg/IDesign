package com.my.interrior.client.pay;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;

@Service
public class PaymentService {

	private IamportClient api;
	
	@Autowired
	private PrePaymentRepository prePaymentRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	
	public PaymentService() {
		this.api = new IamportClient("8471742233220225","6ubc5M8sk1cIS55c74R850heIiuOhE2OH9fU1lJdJGtr4O8hzESYAxxx0ZU0U4nRjvNP1kq1HSAMxvmN");
	}
	
	public void postPrepare(PrePaymentEntity request) throws IamportResponseException, IOException{
		PrepareData prepareData = new PrepareData(request.getMerchant_uid(), request.getAmount());
		
		api.postPrepare(prepareData); // 사전 등록 api
		
		prePaymentRepository.save(request);
	}
	
	public void saveMyPayment(PayEntity pay) throws Exception{
		paymentRepository.save(pay);
	}
}
