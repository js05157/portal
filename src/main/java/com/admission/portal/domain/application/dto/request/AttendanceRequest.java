package com.admission.portal.domain.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record AttendanceRequest(
        @Min(0) Integer absenceCnt,
        @Min(0) Integer tardinessCnt,
        @Min(0) Integer earlyLeaveCnt
) {}
