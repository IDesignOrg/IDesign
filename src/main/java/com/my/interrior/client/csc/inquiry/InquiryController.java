package com.my.interrior.client.csc.inquiry;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InquiryController {

	private final UserRepository userRepository;
	private final InquiryRepository inquiryRepository;
	private final InquiryService inquiryService;
	
	@GetMapping("/board/inquiry/write")
	public String inquiryWrite(HttpSession session, Model model) {
		return "admin/csc/inquiryWrite";
	}
	
	@PostMapping("/board/inquiry/write")
	public String inqWrite(@ModelAttribute InquiryEntity inquiryEntity, Model model, HttpSession session) {
		
		String uid = (String) session.getAttribute("UId");
		UserEntity user = userRepository.findByUId(uid);
		
			inquiryEntity.setInqRegisteredDate(LocalDate.now());
			inquiryEntity.setUserEntity(user);
			inquiryEntity.setInqAuthor(inquiryEntity.getUserEntity().getUName());
			
			inquiryRepository.save(inquiryEntity);
			//임시
			return "redirect:/board/inquiry";

		
	}
	
	@GetMapping("/board/inquiry/category/{category}")
	public String getCategory(@PathVariable("category") String category,
							  @RequestParam(name = "page", defaultValue = "0") int page,
							  @RequestParam(name = "size", defaultValue = "10") int size,
							  Model model) {
		
		Page<InquiryEntity> contents = inquiryService.findByCategory(category, PageRequest.of(page, size));
		
		List<String> categories = inquiryService.getAllCategories();
		model.addAttribute("inquiries", contents);
		model.addAttribute("categories", categories);
		
		return "client/csc/inquiry";
		
	}

	@GetMapping("/board/inquiry/{no}")
	public String getInquiryDetail(@PathVariable("no") Long inqNo, Model model) {
		
		InquiryEntity inq = inquiryService.getDetailByinqNo(inqNo);
		model.addAttribute("inq", inq);
		
		return "client/csc/inquiryDetail";
	}
	
	@GetMapping("/board/inquiry/search")
	public String getInquirySearch(@RequestParam("keyword") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			Model model) {
		
		Pageable pageable = PageRequest.of(page, size);
		Page<InquiryEntity> inqs = inquiryService.getInqByKeyword(keyword, pageable);
		model.addAttribute("categories", inquiryService.getAllCategories());
		model.addAttribute("inquiries", inqs);
		model.addAttribute("param", keyword);
		
		return "client/csc/inquiry";
	}
	
}
