package com.my.interrior.client.csc.inquiry;

import java.time.LocalDate;


import com.my.interrior.client.user.FindUserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InquiryDTO {
	private Long inqNo;
	private String inqTitle;
	private LocalDate inqRegisteredDate;
	private String inqCategory;
	private String inqContent;
	private FindUserDTO user;
	private InquiryAnswerDTO answer;
}
