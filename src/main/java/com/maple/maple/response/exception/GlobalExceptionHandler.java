package com.maple.maple.response.exception;


import com.maple.maple.response.response.ApiResponse;
import com.maple.maple.response.response.ApiResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {

        ApiResponseEnum errorEnum = ex.getApiResponseEnum();

        log.warn("CustomException 발생: [{}] {}", errorEnum.getHttpStatus(), errorEnum.getMessage());

        return ResponseEntity
                .status(errorEnum.getHttpStatus())
                .body(ApiResponse.error(errorEnum.getMessage()));
    }

    // 기타 예외 처리 (NULL 포인터, ILLEGAL ARGUMENT 등)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("서버 내부 예외 발생", ex);
        return ResponseEntity
            .status(500)
            .body(ApiResponse.error("서버 내부 오류가 발생했습니다."));
    }

    // Valid 용
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("잘못된 요청입니다.");

        log.warn("Validation 오류 발생: {}", errorMessage);

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(errorMessage));
    }
}