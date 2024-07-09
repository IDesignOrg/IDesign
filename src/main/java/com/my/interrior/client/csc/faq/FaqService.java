package com.my.interrior.client.csc.faq;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqService {

	private final FaqRepository faqRepository;
	
	public Page<FaqEntity> getFaqs(int page, int size){
		
		Pageable pageable = PageRequest.of(page, size);
		
		return faqRepository.findAll(pageable);
	}
	public FaqEntity getFaqDetailByNo(Long faqNo) {
		return faqRepository.findByfaqNo(faqNo);
	}
	
	public List<String> getAllCategories(){
		
		return faqRepository.findAllfaqCategory();
	}
	
	public Page<FaqEntity> getFaqsByCategory(String category, PageRequest pageable){
		System.out.println("pageble의 값은 : " + pageable);
		return faqRepository.findByfaqCategory(category, pageable);
	}
	
	public Page<FaqEntity> getFaqsByKeyword(String keyword, Pageable pageable){

		Page<FaqEntity> faqs = faqRepository.findByfaqTitleContaining(keyword, pageable);

		return faqs;
	}
}
