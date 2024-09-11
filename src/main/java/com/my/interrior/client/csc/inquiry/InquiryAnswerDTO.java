package com.my.interrior.client.csc.inquiry;

import java.time.LocalDate;

import com.my.interrior.client.user.FindUserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryAnswerDTO {
	private Long ansNo; // 답변 ID
    private String ansContent; // 답변 내용
    private LocalDate ansRegisteredDate; // 답변 작성일자
    private FindUserDTO user; // 답변 작성자 정보
}
