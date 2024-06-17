package com.my.interrior.client.event;

import lombok.Data;

@Data
public class EventDTO {
	private Long eventNo;
	private String eventTitle;
	private String eventContent;
	private String eventImg;
}
