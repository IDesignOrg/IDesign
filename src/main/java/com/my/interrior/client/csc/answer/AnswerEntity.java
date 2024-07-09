package com.my.interrior.client.csc.answer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.my.interrior.client.csc.inquiry.InquiryEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "answer")
@Getter
@Setter
@ToString
public class AnswerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long answerNo;
	
	@Lob
	private String answerContent;
	
	@JoinColumn(name = "inq_no")
	@ManyToOne
	@JsonIgnore
	private InquiryEntity inquiry;
}
