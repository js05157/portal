package com.admission.portal.domain.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequest {
    private List<SubjectScoreRequest> subjectScoreRequestList;
}
