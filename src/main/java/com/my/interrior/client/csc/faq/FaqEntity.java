package com.my.interrior.client.csc.faq;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.interrior.client.user.UserEntity;

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

@Entity(name = "faq")
@Getter
@Setter
@ToString
public class FaqEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long faqNo;
	
	private String faqTitle;
	
	@Lob
	private String faqContent;
	
	@DateTimeFormat(pattern = "yyyy.MM.dd")
	private LocalDate faqRegisteredDate;
	
	private String faqCategory;
	
	private String faqAuthor;
	@ManyToOne
	@JoinColumn(name = "u_no")
	@JsonProperty("UNo")
	private UserEntity userEntity;
}
