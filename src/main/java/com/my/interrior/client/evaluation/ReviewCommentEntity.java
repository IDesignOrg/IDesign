package com.my.interrior.client.evaluation;

import java.time.LocalDateTime;

import com.my.interrior.client.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "review_comment")
@Getter
@Setter
@JsonIgnoreProperties({"reviews", "comments"})
public class ReviewCommentEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rCommentNo;
	
	@Column(name = "r_comment_created")
	private LocalDateTime rCommentCreated;
	
	@Column(columnDefinition = "TEXT", name = "r_comment")
	private String rComment;
	
	@ManyToOne
	@JoinColumn(name = "u_Id", referencedColumnName = "u_id")
	private UserEntity user;
	
	@ManyToOne
	@JoinColumn(name = "r_no")
	@JsonBackReference
	private ReviewEntity reviewEntity;
}
