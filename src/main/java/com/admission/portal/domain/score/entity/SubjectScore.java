package com.admission.portal.domain.score.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "subject_score")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class SubjectScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_score_id")
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "score_id", nullable = false)
    private Score score;

    @Column(nullable = false, length = 50)
    private String subjectName;

    @Column(nullable = false, length = 20)
    private String semester;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal rawScore;

    @Builder
    public SubjectScore(Score score, String subjectName, String semester, BigDecimal rawScore) {
        this.score = score;
        this.subjectName = subjectName;
        this. semester = semester;
        this.rawScore = rawScore;
    }
}
