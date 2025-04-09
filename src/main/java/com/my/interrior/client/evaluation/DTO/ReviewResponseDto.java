package com.my.interrior.client.evaluation.DTO;


import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewResponseDto {
    private Long rno;
    private String rtitle;
    private String rcontent;
    private String rcategory;
    private String rstarRating;
    private Integer rviews;
    private LocalDateTime rwrittenTime;
    private String userId;
    private List<String> reviewPhotos;
    private List<CommentResponseDto> comments;

    public ReviewResponseDto(Long rNo, String rTitle, String rContent, String rCategory, 
                          String rStarRating, Integer rViews, LocalDateTime rWrittenTime, 
                          String userId, List<String> reviewPhotos, List<CommentResponseDto> comments) {
        this.rno = rNo;
        this.rtitle = rTitle;
        this.rcontent = rContent;
        this.rcategory = rCategory;
        this.rstarRating = rStarRating;
        this.rviews = rViews;
        this.rwrittenTime = rWrittenTime;
        this.userId = userId;
        this.reviewPhotos = reviewPhotos;
        this.comments = comments;
    }
}