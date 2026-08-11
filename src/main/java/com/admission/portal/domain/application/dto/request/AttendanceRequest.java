package com.admission.portal.domain.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    private int absenceCnt;
    private int tardinessCnt;
    private int earlyLeaveCnt;
}
