package com.my.interrior.three;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SaveProjectRequest {

	private String projectId;
	private List<DataRequest> dataEntities;
	
	@Getter
	@Setter
	@ToString
	public static class DataRequest{
		private Long oid;
		private String type;
		private Double rotation;
		private List<PointRequest> points;
		private List<DataRequest> children;
	}
	
	@Getter
	@Setter
	@ToString
	public static class PointRequest{
		private Double x;
		private Double y;
		private Double z;
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
