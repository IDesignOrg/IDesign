package com.my.interrior.client.pay;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IamportTokenResponse {
	private String code;
	private String message;
	private IamportTokenResponseData response;
	
	@Getter
	@Setter
	@ToString
	public static class IamportTokenResponseData{
		private String access_token;
	}
}
