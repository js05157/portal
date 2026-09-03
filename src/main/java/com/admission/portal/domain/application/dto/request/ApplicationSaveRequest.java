package com.admission.portal.domain.application.dto.request;

import com.admission.portal.domain.application.entity.Major;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record ApplicationSaveRequest(
        Major major,
        @Valid @NotNull AttendanceRequest attendance,
        @Valid @NotNull ScoreRequest score
) {}
