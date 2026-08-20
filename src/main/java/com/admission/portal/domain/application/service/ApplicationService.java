package com.admission.portal.domain.application.service;

import com.admission.portal.domain.application.dto.request.ApplicationSaveRequest;
import com.admission.portal.domain.application.dto.request.AttendanceRequest;
import com.admission.portal.domain.application.dto.request.SubjectScoreRequest;
import com.admission.portal.domain.application.dto.response.ApplicationDetailResponse;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;
    private final ScoreRepository scoreRepository;

    @Transactional
    public void saveDraft(Long userId, ApplicationSaveRequest request) {
        saveOrUpdate(userId, request, false);
    }

    @Transactional
    public void submit(Long userId, ApplicationSaveRequest request){
        validateForSubmit(request);

        Application application = saveOrUpdate(userId, request, true);

        long currentSubmittedCount = applicationRepository.countByMajorAndStatus(application.getMajor(), ApplicationStatus.SUBMITTED);
        String examineeNumber = String.format("%s-%04d", application.getMajor(), currentSubmittedCount + 1);
        application.submit(examineeNumber);
    }

    private void validateForSubmit(ApplicationSaveRequest request) {
        if (request.getMajor() == null) {
            throw new BusinessException(ErrorCode.MAJOR_REQUIRED);
        }

        AttendanceRequest attendance = request.getAttendance();
        if (attendance == null
                || attendance.getAbsenceCnt() == null
                || attendance.getTardinessCnt() == null
                || attendance.getEarlyLeaveCnt() == null) {
            throw new BusinessException(ErrorCode.ATTENDANCE_REQUIRED);
        }

        List<SubjectScoreRequest> subjectScores = request.getScore().getSubjectScoreRequestList();
        Set<String> submitted = new HashSet<>();
        for (SubjectScoreRequest s : subjectScores) {
            submitted.add(s.getSemester().name() + "-" + s.getSubject().name());
        }

        for (Semester semester : EnumSet.allOf(Semester.class)) {
            for (Subject subject : EnumSet.allOf(Subject.class)) {
                if (!submitted.contains(semester.name() + "-" + subject.name())) {
                    throw new BusinessException(ErrorCode.SUBJECT_SCORE_INCOMPLETE);
                }
            }
        }
    }


    private Application saveOrUpdate(Long userId, ApplicationSaveRequest request, boolean calculateScore){
        Application application = applicationRepository.findByUserId(userId)
                .orElseGet(() -> createApplication(userId));

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

        if (calculateScore) {
            score.updateGpaScore(score.calculateGpaScore());
            score.updateAbsenceScore(attendance.calculateAttendanceScore());
        }
        return application;
    }

    @Transactional(readOnly = true)
    public Optional<ApplicationDetailResponse> getMyApplication(Long userId) {
        return applicationRepository.findByUserId(userId)
                .map(application -> {
                    Attendance attendance = attendanceRepository.findByApplicationId(application.getId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));
                    Score score = scoreRepository.findByApplicationId(application.getId())
                            .orElseThrow(() -> new BusinessException(ErrorCode.APPLICATION_NOT_FOUND));

                    return ApplicationDetailResponse.of(application, attendance, score);
                });
    }

    private Application createApplication(Long userId) {
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
                .absenceCnt(null)
                .tardinessCnt(null)
                .earlyLeaveCnt(null)
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
