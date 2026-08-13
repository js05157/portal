package com.admission.portal.domain.application.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AttendanceTest {
    @Test
    @DisplayName("결석, 지각, 조퇴가 없으므로 만점(20)이 반환된다")
    void calculateAttendanceScore_perfect() {
        //given
        Application mockApplication = mock(Application.class);
        Attendance attendance = Attendance.builder()
                .application(mockApplication)
                .absenceCnt(0)
                .tardinessCnt(0)
                .earlyLeaveCnt(0)
                .build();

        //when
        BigDecimal attendanceScore = attendance.calculateAttendanceScore();

        //then
        assertThat(attendanceScore).isEqualTo(new BigDecimal("20.00"));
    }

    @Test
    @DisplayName("결석 3번, 지각 2번, 조퇴 1번이므로 16점이 반환된다")
    void calculateAttendanceScore_convert() {
        //given
        Application mockApplication = mock(Application.class);
        Attendance attendance = Attendance.builder()
                .application(mockApplication)
                .absenceCnt(3)
                .tardinessCnt(2)
                .earlyLeaveCnt(1)
                .build();

        //when
        BigDecimal attendanceScore = attendance.calculateAttendanceScore();

        //then
        assertThat(attendanceScore).isEqualTo(new BigDecimal("16.00"));
    }
}