package com.admission.portal.domain.application.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    @Min(0)
    private Integer absenceCnt;
    @Min(0)
    private Integer tardinessCnt;
    @Min(0)
    private Integer earlyLeaveCnt;
}
