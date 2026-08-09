package com.admission.portal.domain.score.entity;

import com.admission.portal.domain.application.entity.Application;
import com.admission.portal.global.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.admission.portal.domain.score.entity.Subject.*;

@Entity
@Table(name = "score")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @OneToMany(mappedBy = "score", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<SubjectScore> subjectScores = new ArrayList<>();

    @Column(precision = 5, scale = 2)
    private BigDecimal gpaScore;

    @Column(precision = 5, scale = 2)
    private BigDecimal absenceScore;

    @Column(precision = 5, scale = 2)
    private BigDecimal totalScore;

    @Builder
    public Score(Application application, BigDecimal gpaScore, BigDecimal absenceScore, BigDecimal totalScore){
        this.application = application;
        this.gpaScore = gpaScore;
        this.absenceScore = absenceScore;
        this.totalScore = totalScore;
    }

    public void updateScores(BigDecimal gpaScore, BigDecimal absenceScore) {
        this.gpaScore = gpaScore;
        this.absenceScore = absenceScore;
        this.totalScore = gpaScore.add(absenceScore);
    }

    public void calculateGpaScore() {
        if (this.subjectScores.isEmpty()) {
            this.gpaScore = BigDecimal.ZERO;
        } else {

            BigDecimal totalWeightedScore = BigDecimal.ZERO;
            BigDecimal totalWeight = BigDecimal.ZERO;
            for (SubjectScore subjectScore : this.subjectScores) {

                Subject subject = subjectScore.getSubject();
                BigDecimal subjectWeight = BigDecimal.valueOf(subject.getWeight());
                BigDecimal rawScore = subjectScore.getRawScore();

                Semester semester = subjectScore.getSemester();
                BigDecimal semesterWeight = BigDecimal.valueOf(semester.getWeight());

                BigDecimal weight = subjectWeight.multiply(semesterWeight).divide(BigDecimal.valueOf(100));
                BigDecimal weightedScore = rawScore.multiply(weight);

                totalWeightedScore = totalWeightedScore.add(weightedScore);
                totalWeight = totalWeight.add(weight);
            }

            if(totalWeight.compareTo(BigDecimal.ZERO) == 0){
                this.gpaScore = BigDecimal.ZERO;
            } else {
                BigDecimal gpaAverage = totalWeightedScore.divide(totalWeight, 4, RoundingMode.HALF_UP);
                this.gpaScore = gpaAverage.multiply(new BigDecimal("0.8")).setScale(2, RoundingMode.HALF_UP);
            }
        }
        calculateTotalScore();
    }

    public void updateAbsenceScore(BigDecimal absenceScore) {
        this.absenceScore = (absenceScore != null) ? absenceScore : BigDecimal.ZERO;
        calculateTotalScore();
    }

    public void calculateTotalScore() {
        BigDecimal gpa = (this.gpaScore != null) ? this.gpaScore : BigDecimal.ZERO;
        BigDecimal absence = (this.absenceScore != null) ? this.absenceScore : BigDecimal.ZERO;

        this.totalScore = gpa.add(absence);
    }
}
