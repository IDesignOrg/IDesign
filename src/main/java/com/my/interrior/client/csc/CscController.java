package com.my.interrior.client.csc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.interrior.client.csc.faq.FaqEntity;
import com.my.interrior.client.csc.faq.FaqService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CscController {

	private final FaqService faqService;
	
	@GetMapping("/board/faq")
	public String goCsc(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
									 @RequestParam(name = "size", defaultValue = "10") int size) {
		
		Page<FaqEntity> faqs = faqService.getFaqs(page, size);
		
		List<String> categories = faqService.getAllCategories();
		
		model.addAttribute("categories", categories);
		model.addAttribute("faqs", faqs);
		
		return "client/csc/csc";
	}
}
