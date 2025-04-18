package com.my.interrior.client.evaluation.DTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {
    private Long rcommentNo; // 소문자로 변경
    private String rcomment; // 소문자로 변경
    private LocalDateTime rcommentCreated; // 소문자로 변경
    private String userName; 
    private String userId;
    private String userProfile;
    
    public CommentResponseDto(Long rCommentNo, String rComment, LocalDateTime rCommentCreated, 
                              String userName, String userId, String userProfile) {
        this.rcommentNo = rCommentNo;
        this.rcomment = rComment;
        this.rcommentCreated = rCommentCreated;
        this.userName = userName;
        this.userId = userId;
        this.userProfile = userProfile;
    }
}