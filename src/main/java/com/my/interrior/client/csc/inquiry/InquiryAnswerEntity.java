package com.my.interrior.client.csc.inquiry;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "inquiry_answer")
@Getter
@Setter
public class InquiryAnswerEntity {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ans_no", nullable = false, unique = true)
    private Long ansNo;

    @Lob
    private String ansContent;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate ansRegisteredDate;

    @JoinColumn(name = "inq_no")
    @OneToOne(fetch = FetchType.LAZY)
    @JsonProperty("inqNo")
    private InquiryEntity inquiry;

    @JoinColumn(name = "u_no")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty("UNo")
    private UserEntity userEntity;
}
