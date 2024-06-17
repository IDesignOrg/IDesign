package com.my.interrior.client.evaluation;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.interrior.client.user.UserEntity;

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

@Entity(name = "review")
@Getter
@Setter
@ToString
public class ReviewEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("RNo")
    @Column(name = "r_no")
	private Long RNo;
	
	@JsonProperty("RTitle")
	@Column(nullable = false, name = "r_title")
	private String RTitle;
	
	@JsonProperty("RContent")
	@Column(columnDefinition = "TEXT", name = "r_content")
	private String RContent;

	@JsonProperty("RMainphoto")
	@Column(nullable = false, name = "r_mainphoto")
	private String RMainphoto;

	@JsonProperty("RCategory")
	@Column(name = "r_category")
	private String RCategory;

	@JsonProperty("RStarRating")
	@Column(name = "r_star_rating")
	private String RStarRating;

	@JsonProperty("RViews")
	@Column(name = "r_views")
	private Integer RViews;

	@JsonProperty("RWrittenTime")
	@Column(name = "r_written_time", nullable = false)
	private LocalDateTime RWrittenTime;

	@ManyToOne
	@JoinColumn(name = "u_id", referencedColumnName = "u_id")
	private UserEntity user;

	@JsonProperty("RProfile")
	@Column(name = "r_profile")
	private String RProfile;
	
}
