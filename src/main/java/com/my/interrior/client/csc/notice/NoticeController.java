package com.my.interrior.client.csc.notice;

import java.time.LocalDate;

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
public class NoticeController {

	private final UserRepository userRepository;
	private final NoticeRepository noticeRepository;
	private final NoticeService noticeService;
	
	@GetMapping("/board/notice/write")
	public String goToWrite() {
		return "admin/csc/noticeWrite";
	}
	
	@PostMapping("/board/notice/write")
	public String notWrite(@ModelAttribute NoticeEntity noticeEntity, HttpSession session) {
		
		noticeEntity.setNotRegisteredDate(LocalDate.now());
		String uid = (String) session.getAttribute("UId");
		UserEntity user = userRepository.findByUId(uid);
		noticeEntity.setUserEntity(user);
		noticeEntity.setNotAuthor(user.getUName());
		
		noticeRepository.save(noticeEntity);
		//나중에 변경할 것
		return "redirect:/admin/page/adminNotice";
		
	}
	
	@GetMapping("/board/notice/{no}")
	public String noticeDetail(@PathVariable("no") Long notNo, Model model) {
		
		NoticeEntity notice = noticeService.getNoticeDetail(notNo);
		
		model.addAttribute("notice", notice);
		
		return "client/csc/noticeDetail";
		
	}
	
	@GetMapping("/board/notice/search")
	public String getNoticesByKeyword(@RequestParam("keyword") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			Model model) {
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<NoticeEntity> notices = noticeService.getNoticeByKeyword(keyword, pageable);
		model.addAttribute("notices", notices);
		model.addAttribute("param", keyword);
		
		return "client/csc/notices";
	}
	
}
