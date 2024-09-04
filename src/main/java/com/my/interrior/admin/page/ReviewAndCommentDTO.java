package com.my.interrior.admin.page;

import com.my.interrior.client.evaluation.ReviewEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewAndCommentDTO {
	private ReviewEntity review;
	private int commentCount;
	
	public ReviewAndCommentDTO(ReviewEntity review, int commentCount) {
		this.review = review;
		this.commentCount = commentCount;
	}
}
