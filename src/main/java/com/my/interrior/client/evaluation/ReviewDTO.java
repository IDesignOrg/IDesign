package com.my.interrior.client.evaluation;

import java.time.LocalDateTime;

import com.my.interrior.client.user.UserEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDTO {

	private Long RNo;
	
	private String RTitle;
	
	private String RContent;
	
	private String RMainphoto;
	
	private String RCategory;
	
	private String RStarRating;
	
	private Integer RViews;
	
	private LocalDateTime RWrittenTime;
	
	private UserEntity user;
	
	private String RProfile;
}
