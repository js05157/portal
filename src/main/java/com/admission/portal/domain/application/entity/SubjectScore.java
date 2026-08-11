package com.admission.portal.domain.application.entity;

import com.admission.portal.global.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "subject_score")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SubjectScore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_score_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_id", nullable = false)
    private Score score;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Semester semester;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rawScore;

    @Builder
    public SubjectScore(Score score, Subject subject, Semester semester, BigDecimal rawScore) {
        this.score = score;
        this.subject = subject;
        this.semester = semester;
        this.rawScore = rawScore;
    }
}
