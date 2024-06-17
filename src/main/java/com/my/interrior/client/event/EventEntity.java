package com.my.interrior.client.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "event")
public class EventEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "event_no")
	private Long eventNo;

	@Column(nullable = false, name = "event_title")
	private String eventTitle;

	@Column(nullable = false, columnDefinition = "TEXT", name = "event_content")
	private String eventContent;

	@Column(nullable = false, name = "event_img")
	private String eventImg;
}
