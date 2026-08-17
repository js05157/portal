package com.admission.portal.domain.application.dto.request;

import com.admission.portal.domain.application.entity.Major;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSaveRequest {
    private Major major;
    private AttendanceRequest attendance;
    private ScoreRequest score;
}
