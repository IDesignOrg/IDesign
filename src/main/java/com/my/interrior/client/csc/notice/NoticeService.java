package com.my.interrior.client.csc.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeRepository noticeRepository;
	
	public Page<NoticeEntity> getNotices(int page, int size){
		Pageable pageable = PageRequest.of(page, size);
		
		return noticeRepository.findAll(pageable);
	}
	
	public NoticeEntity getNoticeDetail(Long notNo) {
		return noticeRepository.findBynotNo(notNo);
	}
	public Page<NoticeEntity> getNoticeByKeyword(String keyword, Pageable pageable){
		return noticeRepository.findBynotTitleContaining(keyword, pageable);
	}
}
