package com.my.interrior.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NaverOAuthToken {

	private String access_token;
	
    private String refresh_token;
    
    private String token_type;
    
    private String expires_in;
}
