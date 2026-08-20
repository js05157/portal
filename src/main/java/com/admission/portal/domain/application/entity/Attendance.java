package com.admission.portal.domain.application.entity;

import com.admission.portal.global.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "attendance")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    private Integer absenceCnt;

    private Integer tardinessCnt;

    private Integer earlyLeaveCnt;

    @Builder
    public Attendance (Application application, Integer absenceCnt, Integer tardinessCnt, Integer earlyLeaveCnt){
        this.application = application;
        this.absenceCnt = absenceCnt;
        this.tardinessCnt = tardinessCnt;
        this.earlyLeaveCnt = earlyLeaveCnt;
    }

    public void updateAttendance(Integer absenceCnt, Integer tardinessCnt, Integer earlyLeaveCnt) {
        this.absenceCnt = absenceCnt;
        this.tardinessCnt = tardinessCnt;
        this.earlyLeaveCnt = earlyLeaveCnt;
    }

    public BigDecimal calculateAttendanceScore() {
        int convertedAbsenceDays = this.absenceCnt + (this.tardinessCnt + this.earlyLeaveCnt) / 3;

        int score = Math.max(0, 20 - convertedAbsenceDays);
        return BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP);
    }
}
