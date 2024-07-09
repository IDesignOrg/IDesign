package com.my.interrior.client.csc.inquiry;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.interrior.client.csc.answer.AnswerEntity;
import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "inquiry")
@Getter
@Setter
@ToString(exclude = {"answer"})
public class InquiryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long inqNo;
	private String inqTitle;

	@DateTimeFormat(pattern = "yyyy.MM.dd")
	private LocalDate inqRegisteredDate;
	private String inqCategory;

	@Lob
	private String inqContent;

	@Column(nullable = false)
	private String inqAuthor;

	@JoinColumn(name = "u_no")
	@ManyToOne
	@JsonProperty("UNo")
	private UserEntity userEntity;

	@OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AnswerEntity> answer;

}
