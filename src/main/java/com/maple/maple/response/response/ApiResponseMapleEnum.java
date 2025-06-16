package com.maple.maple.response.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResponseMapleEnum implements ApiResponseEnum {
    // 200
    TEAM_OCID_GET_SUCCESS(HttpStatus.OK,"팀원 ocid 조회에 성공하였습니다."),


    // 400
    MEMBER_NOT_EXIST(HttpStatus.BAD_REQUEST,"회원으로 등록되지 않은 이메일 입니다."),


    //401
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다.");


    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ApiResponseMapleEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
        code = httpStatus.value();
    }
}
