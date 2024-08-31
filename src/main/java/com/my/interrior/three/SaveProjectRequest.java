package com.my.interrior.three;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaveProjectRequest {

	private String projectId;
	private List<DataRequest> dataEntities;
	private MultipartFile thumbnail;
	private ProjectRequest projectSrc;
	
	@Getter
	@Setter
	@ToString
	public static class DataRequest{
		private String oid;
		private String type;
		private Double rotation;
		private String parent;
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
	
//	private long userNo;
//	private String projectId;
//	private List<DataEntityRequest> dataEntities;
//	
//	@Getter
//	@Setter
//	@ToString
//	public static class DataEntityRequest {
//		private String type;
//		private Double rotation;
//		private Double angle;
//		private List<PointRequest> points;
//		private List<DataEntityRequest> children = new ArrayList<>();
//	}
//	
//	@Getter
//	@Setter
//	@ToString
//	public static class PointRequest{
//		private Double x;
//		private Double y;
//		private Double z;
//	}
}
