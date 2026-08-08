package com.admission.portal.domain.attendance.entity;

import com.admission.portal.domain.application.entity.Application;
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
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @Column(nullable = false)
    private int absenceCnt;

    @Column(nullable = false)
    private int tardinessCnt;

    @Column(nullable = false)
    private int earlyLeaveCnt;

    @Builder
    public Attendance (Application application, int absenceCnt, int tardinessCnt, int earlyLeaveCnt){
        this.application = application;
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
