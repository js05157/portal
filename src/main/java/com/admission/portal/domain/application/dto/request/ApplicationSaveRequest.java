package com.admission.portal.domain.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSaveRequest {
    private String major;
    private AttendanceRequest attendance;
    private ScoreRequest score;
}
