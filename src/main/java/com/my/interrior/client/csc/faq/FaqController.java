package com.my.interrior.client.csc.faq;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class FaqController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FaqRepository faqRepository;
	
	@GetMapping("/board/faqWrite")
	public String faqWrite() {
		return "admin/csc/faqWrite";
	}
	
	@PostMapping("/board/faqWrite")
	public String faqWrite(@ModelAttribute FaqEntity faq, HttpSession session) {

		faq.setFaqRegisteredDate(LocalDate.now());
		//이름도 가져오기
		String UId = (String) session.getAttribute("UId");
		UserEntity user = userRepository.findByUId(UId);
		faq.setFaqAuthor(user.getUName());
		faq.setUserEntity(user);
		
		faqRepository.save(faq);
		
		return "admin/csc/faqWrite";
		
	}
}
