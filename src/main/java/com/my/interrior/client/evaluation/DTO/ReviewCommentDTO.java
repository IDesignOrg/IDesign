package com.my.interrior.client.evaluation.DTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCommentDTO {
	private Long rCommentNo;
    private String rComment;
    private LocalDateTime rCommentCreated;
    private String userName;
    private String userId;
    private String userProfile;
    
    public ReviewCommentDTO(Long rCommentNo, String rComment, LocalDateTime rCommentCreated,  String userName, String userId,String userProfile) {
        this.rCommentNo = rCommentNo;
        this.rComment = rComment;
        this.rCommentCreated = rCommentCreated;
        this.userName = userName;
        this.userId = userId;
        this.userProfile = userProfile;
    }
}
