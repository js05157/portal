package com.admission.portal.global.error;

import org.springframework.validation.FieldError;

import java.util.List;

public record ValidationErrorResponse(
        String code,
        String message,
        List<ValidationFieldError> errors
) {
    public static ValidationErrorResponse of(List<ValidationFieldError> errors) {
        return new ValidationErrorResponse("INVALID_INPUT", "입력값이 올바르지 않습니다.", errors);
    }

    public record ValidationFieldError(
            String field,
            String reason
    ) {
        public static ValidationFieldError from(FieldError fieldError) {
            return new ValidationFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }

}
