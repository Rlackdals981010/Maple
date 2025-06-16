package com.maple.maple.response.exception;


import com.maple.maple.response.response.ApiResponseEnum;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ApiResponseEnum apiResponseEnum;

    public CustomException(ApiResponseEnum apiResponseEnum) {
        super(apiResponseEnum.getMessage());
        this.apiResponseEnum = apiResponseEnum;
    }
}