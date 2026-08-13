package com.admission.portal.domain.application.dto.request;

import com.admission.portal.domain.application.entity.Grade;
import com.admission.portal.domain.application.entity.Semester;
import com.admission.portal.domain.application.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectScoreRequest {
    private Semester semester;
    private Subject subject;
    private Grade grade;
}
