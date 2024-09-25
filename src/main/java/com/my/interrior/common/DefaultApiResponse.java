package com.my.interrior.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

//Author:한민욱
//Swagger 작성하기 쉽게 만듦.
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "성공"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음") })
public @interface DefaultApiResponse {
}
