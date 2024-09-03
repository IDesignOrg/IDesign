package com.my.interrior.client.csc.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswerEntity, Long> {
	InquiryAnswerEntity findByInquiryInqNo(Long inqNo);
}
