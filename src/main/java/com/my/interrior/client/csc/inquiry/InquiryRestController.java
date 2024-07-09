package com.my.interrior.client.csc.inquiry;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class InquiryRestController {

	private final InquiryService inquiryService;

//	@GetMapping("/board/inquiry/{no}")
//	public ResponseEntity<InquiryEntity> getInquiryDetail(@PathVariable("no") Long inqNo, Model model,
//			HttpSession session) {
//
//		String uid = (String) session.getAttribute("UId");
//
//		InquiryEntity inq = inquiryService.getDetailByinqNo(inqNo);
//		if (uid != null && uid.equals(inq.getUserEntity().getUId())) {
//
//			return ResponseEntity.ok(inq);
//		} else if(uid != null && !uid.equals(inq.getUserEntity().getUId())){
//			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//		}else {
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
//		}
//	}
}
