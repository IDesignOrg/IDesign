package com.my.interrior.client.csc.notice;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NoticeRestController {

//	private final NoticeService noticeService;
	
//	@GetMapping("/auth/notices")
//	public Page<NoticeEntity> showNotices(@RequestParam(defaultValue = "0") int page,
//										  @RequestParam(defaultValue = "10") int size){
//		
//		return noticeService.getNotices(page, size);
//	}
}
