package com.my.interrior.error;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.my.interrior.common.CommonResponse;

//Author: 한민욱
//예외 처리 이걸로 해결하기.
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 Bad Request - 잘못된 요청 처리
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(CommonResponse.failure("잘못된 요청: " + ex.getMessage()));
    }

    // 404 Not Found - 리소스를 찾을 수 없는 경우 처리
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonResponse<String>> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(CommonResponse.failure("리소스를 찾을 수 없습니다: " + ex.getMessage()));
    }
    
    // 그 외 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<String>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(CommonResponse.failure("서버 오류: " + ex.getMessage()));
    }
}
