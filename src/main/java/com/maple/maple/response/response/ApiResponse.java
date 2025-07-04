package com.maple.maple.response.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private enum Status {
        SUCCESS, FAIL, ERROR
    }

    private final Status status;
    private final T data;
    private final String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(Status.SUCCESS, data, "요청이 성공적으로 처리되었습니다");
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(Status.SUCCESS, data, message);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(Status.SUCCESS,null, message);
    }

    public static ApiResponse<?> successWithNoContent() {
        return new ApiResponse<>(Status.SUCCESS, null, "요청이 성공적으로 처리되었지만 내용이 없습니다");
    }


    public static ApiResponse<?> error(String message) {
        return new ApiResponse<>(Status.FAIL, "", message);
    }
}