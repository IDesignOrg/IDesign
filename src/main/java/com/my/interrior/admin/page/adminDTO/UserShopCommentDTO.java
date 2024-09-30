package com.my.interrior.admin.page.adminDTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserShopCommentDTO {
	private Long shopReviewNo;
    private String shopReviewContent;
    private LocalDateTime shopReviewCreated;
    private Double shopReviewStarRating;
    
    public UserShopCommentDTO(Long shopReviewNo, String shopReviewContent, LocalDateTime shopReviewCreated, Double shopReviewStarRating) {
        this.shopReviewNo = shopReviewNo;
        this.shopReviewContent = shopReviewContent;
        this.shopReviewCreated = shopReviewCreated;
        this.shopReviewStarRating = shopReviewStarRating;
    }
}
