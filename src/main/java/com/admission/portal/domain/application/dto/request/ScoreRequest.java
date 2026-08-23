package com.admission.portal.domain.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public record ScoreRequest(
        List<SubjectScoreRequest> subjectScoreRequestList
) {}
