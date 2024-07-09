package com.my.interrior.client.csc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.interrior.client.csc.faq.FaqEntity;
import com.my.interrior.client.csc.faq.FaqService;
import com.my.interrior.client.csc.inquiry.InquiryEntity;
import com.my.interrior.client.csc.inquiry.InquiryService;
import com.my.interrior.client.csc.notice.NoticeEntity;
import com.my.interrior.client.csc.notice.NoticeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CscController {

	private final FaqService faqService;
	private final NoticeService noticeService;
	private final InquiryService inquiryService;
	
	@GetMapping("/board/faq")
	public String goCsc(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
									 @RequestParam(name = "size", defaultValue = "10") int size) {
		
		Page<FaqEntity> faqs = faqService.getFaqs(page, size);
		
		List<String> categories = faqService.getAllCategories();
		
		model.addAttribute("categories", categories);
		model.addAttribute("faqs", faqs);
		
		return "client/csc/csc";
	}
	
	@GetMapping("/board/notices")
	public String goNotices(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			 @RequestParam(name = "size", defaultValue = "10") int size) {
		
		Page<NoticeEntity> notices = noticeService.getNotices(page, size);
		
		model.addAttribute("notices", notices);
		
		return "client/csc/notices";
	}
	
	@GetMapping("/board/inquiry")
	public String goInquiry(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
			 @RequestParam(name = "size", defaultValue = "10") int size) {
		
		Page<InquiryEntity> inquiry = inquiryService.getInquiry(page, size);
		List<String> categories = inquiryService.getAllCategories();
		model.addAttribute("inquiries", inquiry);
		model.addAttribute("categories", categories);
		return "client/csc/inquiry";
	}
}
