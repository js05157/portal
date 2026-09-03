package com.admission.portal.domain.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public record ScoreRequest(
        @NotNull List<@Valid SubjectScoreRequest> subjectScoreRequestList
) {}
