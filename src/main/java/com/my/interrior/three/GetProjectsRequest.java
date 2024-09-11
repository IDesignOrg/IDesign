package com.my.interrior.three;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetProjectsRequest {

	private String filter;
	private String sort;
	private int flag;

}
