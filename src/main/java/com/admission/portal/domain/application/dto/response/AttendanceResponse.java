package com.admission.portal.domain.application.dto.response;

import com.admission.portal.domain.application.entity.Attendance;

public record AttendanceResponse(
        int absenceCnt,
        int tardinessCnt,
        int earlyLeaveCnt
) {
    public static AttendanceResponse from(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getAbsenceCnt(),
                attendance.getTardinessCnt(),
                attendance.getEarlyLeaveCnt()
        );
    }
}
