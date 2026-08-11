package com.admission.portal.domain.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectScoreRequest {
    private String semester;
    private String subject;
    private String grade;
}
