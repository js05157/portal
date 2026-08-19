package com.admission.portal.domain.application.dto.response;

import com.admission.portal.domain.application.entity.Grade;
import com.admission.portal.domain.application.entity.Score;
import com.admission.portal.domain.application.entity.Semester;
import com.admission.portal.domain.application.entity.Subject;

import java.util.List;

public record ScoreResponse(
        List<SubjectScoreResponse> subjectScores
) {
    public static ScoreResponse from(Score score) {
        List<SubjectScoreResponse> subjectScores = score.getSubjectScores().stream()
                .map(s -> new SubjectScoreResponse(s.getSemester(), s.getSubject(), s.getGrade()))
                .toList();

        return new ScoreResponse(subjectScores);
    }
    public record SubjectScoreResponse(
            Semester semester,
            Subject subject,
            Grade grade
    ) {}
}
