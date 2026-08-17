package com.admission.portal.domain.application.service;

import com.admission.portal.domain.application.dto.request.ApplicationSaveRequest;
import com.admission.portal.domain.application.dto.request.AttendanceRequest;
import com.admission.portal.domain.application.dto.request.SubjectScoreRequest;
import com.admission.portal.domain.application.entity.*;
import com.admission.portal.domain.application.repository.ApplicationRepository;
import com.admission.portal.domain.application.repository.AttendanceRepository;
import com.admission.portal.domain.application.repository.ScoreRepository;
import com.admission.portal.domain.user.entity.User;
import com.admission.portal.domain.user.repository.UserRepository;
import com.admission.portal.global.error.BusinessException;
import com.admission.portal.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ScoreRepository scoreRepository;

    @Transactional
    public void saveDraft(Long userId, ApplicationSaveRequest request) {
        saveOrUpdate(userId, request);
    }

    @Transactional
    public void submit(Long userId, ApplicationSaveRequest request){
        Application application = saveOrUpdate(userId, request);

        long currentSubmittedCount = applicationRepository.countByMajorAndStatus(application.getMajor(), ApplicationStatus.SUBMITTED);
        String examineeNumber = String.format("%s-%04d", application.getMajor(), currentSubmittedCount + 1);
        application.submit(examineeNumber);
    }

    @Transactional
    public Application saveOrUpdate(Long userId, ApplicationSaveRequest request){
        Application application = applicationRepository.findByUserId(userId)
                .orElseGet(() -> createApplication(userId, request));

        application.updateMajor(request.getMajor());

        Attendance attendance = attendanceRepository.findByApplicationId(application.getId())
                .orElseGet(() -> createAttendance(application));

        AttendanceRequest attendanceRequest = request.getAttendance();
        attendance.updateAttendance(attendanceRequest.getAbsenceCnt(), attendanceRequest.getTardinessCnt(), attendanceRequest.getEarlyLeaveCnt());

        Score score = scoreRepository.findByApplicationId(application.getId())
                .orElseGet(() -> createScore(application));

        score.getSubjectScores().clear();
        for(SubjectScoreRequest s : request.getScore().getSubjectScoreRequestList()) {
            score.getSubjectScores().add(
                    SubjectScore.builder()
                            .score(score)
                            .subject(s.getSubject())
                            .semester(s.getSemester())
                            .grade(s.getGrade())
                            .build()
            );
        }

        score.updateGpaScore(score.calculateGpaScore());
        score.updateAbsenceScore(attendance.calculateAttendanceScore());

        return application;
    }

    private Application createApplication(Long userId, ApplicationSaveRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Application application = Application.builder()
                .user(user)
                .status(ApplicationStatus.DRAFT)
                .build();
        return applicationRepository.save(application);
    }

    private Attendance createAttendance(Application application) {
        return attendanceRepository.save(Attendance.builder()
                .application(application)
                .absenceCnt(0)
                .tardinessCnt(0)
                .earlyLeaveCnt(0)
                .build()
        );
    }

    private Score createScore(Application application) {
        return scoreRepository.save(Score.builder()
                .application(application)
                .build()
        );
    }
}
