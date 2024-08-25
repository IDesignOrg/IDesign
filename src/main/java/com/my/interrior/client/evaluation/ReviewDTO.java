package com.my.interrior.client.evaluation;


import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDTO {
	private String rCategory;
    private String rTitle;
    private String rStarRating;
    private String rContent;
    private int rViews;
    private String rWrittenTime;
    private String userUId; // 유저 ID 필드 추가
    private List<String> reviewPhotos;
    private List<ReviewCommentEntity> comments;

    // DTO 생성자
    public ReviewDTO(ReviewEntity review, List<String> reviewPhotos, List<ReviewCommentEntity> comments) {
        this.rCategory = review.getRCategory();
        this.rTitle = review.getRTitle();
        this.rStarRating = review.getRStarRating();
        this.rContent = review.getRContent();
        this.rViews = review.getRViews();
        this.rWrittenTime = review.getRWrittenTime().toString();  // 변환 확인
        this.userUId = review.getUser().getUId();  // 유저 ID 설정 확인
        this.reviewPhotos = reviewPhotos;
        this.comments = comments;
    }
}