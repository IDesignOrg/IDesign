package com.my.interrior.client.evaluation;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity (name = "review_photo")
@Getter
@Setter
@ToString
public class ReviewPhotoEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("RpNo")
	@Column(name = "rp_no")
	private Long RpNo;
	
	@JsonProperty("RpPhoto")
	@Column(name = "rp_photo")
	private String RpPhoto;
	
	@ManyToOne
	@JoinColumn(name = "r_no", referencedColumnName = "r_no")
	private ReviewEntity review;
}
