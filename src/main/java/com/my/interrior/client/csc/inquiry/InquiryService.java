package com.my.interrior.client.csc.inquiry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InquiryService {

	private final InquiryRepository inquiryRepository;
	
	public Page<InquiryEntity> getInquiry(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		
		return inquiryRepository.findAll(pageable);
	}
	
	public List<String> getAllCategories(){
		return inquiryRepository.findAllCategory();
	}
	
	public Page<InquiryEntity> findByCategory(String category, PageRequest pageable){
		return inquiryRepository.findByinqCategory(category, pageable);
	}
	
	public InquiryEntity getDetailByinqNo(Long inqNo) {
		return inquiryRepository.findByinqNo(inqNo);
	}
}
