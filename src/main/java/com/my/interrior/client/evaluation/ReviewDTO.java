package com.my.interrior.client.evaluation;


import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDTO {
	private Long rNo;
    private String rTitle;
    private String rContent;
    private String rCategory;
    private String rStarRating;
    private Integer rViews;
    private LocalDateTime rWrittenTime;
    private String userId;
    private List<String> reviewPhotos;
    private List<ReviewCommentDTO> comments;

    // DTO 생성자
    public ReviewDTO(Long rNo, String rTitle, String rContent, String rCategory, String rStarRating, Integer rViews, LocalDateTime rWrittenTime, String userId, List<String> reviewPhotos, List<ReviewCommentDTO> comments) {
        this.rNo = rNo;
        this.rTitle = rTitle;
        this.rContent = rContent;
        this.rCategory = rCategory;
        this.rStarRating = rStarRating;
        this.rViews = rViews;
        this.rWrittenTime = rWrittenTime;
        this.userId = userId;
        this.reviewPhotos = reviewPhotos;
        this.comments = comments;
    }
}