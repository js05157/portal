package com.admission.portal.domain.score.entity;

import com.admission.portal.domain.application.entity.Application;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "score")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Score {

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

}
