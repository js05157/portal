package com.admission.portal.domain.application.dto.response;

import com.admission.portal.domain.application.entity.*;

public record ApplicationDetailResponse(
        Major major,
        ApplicationStatus status,
        AttendanceResponse attendance,
        ScoreResponse score
){
    public static ApplicationDetailResponse of(Application application, Attendance attendance, Score score) {
        return new ApplicationDetailResponse(
                application.getMajor(),
                application.getStatus(),
                AttendanceResponse.from(attendance),
                ScoreResponse.from(score)
        );
    }

}
