package com.admission.portal.domain.application.dto.request;

import com.admission.portal.domain.application.entity.Major;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationSaveRequest {
    private Major major;
    @Valid
    @NotNull
    private AttendanceRequest attendance;
    @NotNull
    private ScoreRequest score;
}
