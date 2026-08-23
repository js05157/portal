package com.admission.portal.domain.application.dto.request;

import com.admission.portal.domain.application.entity.Grade;
import com.admission.portal.domain.application.entity.Semester;
import com.admission.portal.domain.application.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record SubjectScoreRequest(
        Semester semester,
        Subject subject,
        Grade grade
) {}
