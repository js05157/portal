package com.admission.portal.domain.application.dto.request;

import com.admission.portal.domain.application.entity.Grade;
import com.admission.portal.domain.application.entity.Semester;
import com.admission.portal.domain.application.entity.Subject;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record SubjectScoreRequest(
        @NotNull Semester semester,
        @NotNull Subject subject,
        Grade grade
) {}
