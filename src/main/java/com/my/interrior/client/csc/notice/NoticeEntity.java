package com.my.interrior.client.csc.notice;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.Column;
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

@Entity(name = "notice")
@Getter
@Setter
@ToString
public class NoticeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notNo;
	
	@Column(nullable = false)
	private String notTitle;
	
	@Lob
	private String notContent;
	
	@DateTimeFormat(pattern = "yyyy.MM.dd")
	private LocalDate notRegisteredDate;
	
	@Column(nullable = false)
	private String notAuthor;
	
	@ManyToOne
	@JoinColumn(name = "u_no")
	@JsonProperty("UNo")
	private UserEntity userEntity;
	
}
