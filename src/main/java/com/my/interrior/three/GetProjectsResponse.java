package com.my.interrior.three;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetProjectsResponse {

	private String status;
	private int response;
	private GetProjectDataResponse data;
	
	
	@Getter
	@Setter
	@ToString
	public static class GetProjectDataResponse {
		private List<GetProjectsData> projects;
		private int flagNum;
	}
	
	@Getter
	@Setter
	@ToString
	public static class GetProjectsData {
		private String projectId;
		private String thumbnail;
		private String title;
		private String src;
		private LocalDateTime regDt;
	}
	
}
