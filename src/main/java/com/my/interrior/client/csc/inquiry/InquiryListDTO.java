package com.my.interrior.client.csc.inquiry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryListDTO {
	private InquiryEntity inquiry; // InquiryEntity를 직접 포함
	private boolean hasAnswer; // 답변 여부를 나타내는 필드

	// InquiryEntity를 받아서 필드를 초기화하는 생성자
	public InquiryListDTO(InquiryEntity inquiry, boolean hasAnswer) {
        this.inquiry = inquiry;
        this.hasAnswer = hasAnswer;  // 서비스 계층에서 답변 여부를 설정
    }
}
