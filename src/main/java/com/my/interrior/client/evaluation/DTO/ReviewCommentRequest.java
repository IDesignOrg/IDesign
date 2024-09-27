package com.my.interrior.client.evaluation.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCommentRequest {
	private Long reviewId;
	private String comment;
	
}
