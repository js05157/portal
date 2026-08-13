package com.admission.portal.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 원서입니다."),
    ALREADY_SUBMITTED(HttpStatus.CONFLICT, "이미 최종 제출된 원서입니다.");

    private final HttpStatus status;
    private final String message;
}
