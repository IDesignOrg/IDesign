package com.my.interrior.three;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.my.interrior.config.SingleOrListDeserializer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaveProjectRequest {

	private String project_id;
	private List<DataRequest> dataEntities;
	private ProjectRequest projectSrc;
	
	@Getter
	@Setter
	@ToString
	public static class DataRequest{
		private String oid;
		private String type;
		private Double rotation;
		private String parent;
		@JsonDeserialize(using = SingleOrListDeserializer.class)
		private List<PointRequest> points;
		private List<String> children;
	}
	
	@Getter
	@Setter
	@ToString
	public static class PointRequest{
		private Double x;
		private Double y;
		private Double z;
	}
	
	@Getter
	@Setter
	@ToString
	public static class ProjectRequest{
		private String title;
		private String src;
	}
}
