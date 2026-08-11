package com.admission.portal.domain.score.entity;

import com.admission.portal.domain.application.entity.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ScoreTest {

    @Test
    @DisplayName("과목별 성적이 주어지면 가중치를 반영하여 GPA 점수를 정확히 산출한다.")
    void calculateGpaScore() {
        //given
        Score score = Score.builder()
                .application(mock(Application.class))
                .build();

        SubjectScore subject1 = SubjectScore.builder()
                .score(score)
                .subject(Subject.MATH)
                .semester(Semester.SEMESTER_3_1)
                .grade(Grade.A)
                .build();

        score.getSubjectScores().add(subject1);

        //when
        score.calculateGpaScore();

        //then
        assertThat(score.getGpaScore()).isEqualTo(new BigDecimal("80.00"));
    }

    @Test
    @DisplayName("GPA 점수와 출결 점수가 합산되어 총점이 계산된다.")
    void calculateTotalScore() {

        //given
        Score score = Score.builder()
                .application(mock(Application.class))
                .gpaScore(new BigDecimal("80.00"))
                .build();

        //when
        score.updateAbsenceScore(new BigDecimal("18.50"));

        // then
        assertThat(score.getAbsenceScore()).isEqualByComparingTo(new BigDecimal("18.50"));
        assertThat(score.getTotalScore()).isEqualByComparingTo(new BigDecimal("98.50"));
    }
}