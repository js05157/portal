package com.admission.portal.domain.application.service;

import com.admission.portal.domain.application.dto.request.ApplicationSaveRequest;
import com.admission.portal.domain.application.dto.request.AttendanceRequest;
import com.admission.portal.domain.application.dto.request.ScoreRequest;
import com.admission.portal.domain.application.dto.request.SubjectScoreRequest;
import com.admission.portal.domain.application.entity.*;
import com.admission.portal.domain.application.repository.ApplicationRepository;
import com.admission.portal.domain.application.repository.AttendanceRepository;
import com.admission.portal.domain.application.repository.ScoreRepository;
import com.admission.portal.domain.user.entity.Role;
import com.admission.portal.domain.user.entity.User;
import com.admission.portal.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ApplicationServiceTest {
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;

    private final List<Long> userIds = new ArrayList<>();

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 100; i++) {
            User user = User.builder()
                    .email("test" + i + "@email.com")
                    .password("pw")
                    .name("student" + i)
                    .phone("010-0000-" + String.format("%04d", i))
                    .role(Role.STUDENT)
                    .build();

            Long userId = userRepository.save(user).getId();
            userIds.add(userId);

            applicationService.saveDraft(userId, buildRequest());
        }
    }

    @AfterEach
    void tearDown() {
        scoreRepository.deleteAll();
        attendanceRepository.deleteAll();
        applicationRepository.deleteAll();
        userRepository.deleteAll();
    }

    private ApplicationSaveRequest buildRequest() {
        AttendanceRequest attendanceRequest = new AttendanceRequest(0, 0, 0);

        List<SubjectScoreRequest> subjectScoreRequestList = new ArrayList<>();
        for (Semester semester : Semester.values()) {
            for (Subject subject : Subject.values()) {
                subjectScoreRequestList.add(new SubjectScoreRequest(semester, subject, Grade.A));
            }
        }

        ScoreRequest scoreRequest = new ScoreRequest(subjectScoreRequestList);
        return new ApplicationSaveRequest(Major.SOFTWARE, attendanceRequest, scoreRequest);
    }

//    @Test
//    @DisplayName("동시성 이슈 발생: 100명이 동시에 제출하면 수험번호가 중복 발급된다.")
//    void submit_concurrency_issue() throws InterruptedException {
//        int threadCount = 100;
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        List<Long> failedUserIds = java.util.Collections.synchronizedList(new ArrayList<>());
//
//        for (Long userId : userIds) {
//            executorService.submit(() -> {
//                try {
//                    applicationService.submit(userId, buildRequest());
//                } catch (Exception e) {
//                    failedUserIds.add(userId);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        List<String> issuedExamineeNumbers = applicationRepository.findAll().stream()
//                .map(a -> a.getExamineeNumber())
//                .filter(n -> n != null)
//                .toList();
//
//        long uniqueCount = issuedExamineeNumbers.stream().distinct().count();
//
//        System.out.println("=========================================");
//        System.out.println("발급된 전체 수험번호 수: " + issuedExamineeNumbers.size());
//        System.out.println("중복을 제거한 고유 수험번호 수: " + uniqueCount);
//        System.out.println("=========================================");
//
//        assertThat(failedUserIds.size()).isGreaterThan(0);
//    }

    @Test
    @DisplayName("동시성 제어 적용 후: 100명이 동시에 제출해도 수험번호가 중복 발급되지 않는다.")
    void submit_concurrency_resolved() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Long> failedUserIds = java.util.Collections.synchronizedList(new ArrayList<>());

        for (Long userId : userIds) {
            executorService.submit(() -> {
                try {
                    applicationService.submit(userId, buildRequest());
                } catch (Exception e) {
                    failedUserIds.add(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        List<String> issuedExamineeNumbers = applicationRepository.findAll().stream()
                .map(a -> a.getExamineeNumber())
                .filter(n -> n != null)
                .toList();

        long uniqueCount = issuedExamineeNumbers.stream().distinct().count();

        System.out.println("=========================================");
        System.out.println("발급된 전체 수험번호 수: " + issuedExamineeNumbers.size());
        System.out.println("중복을 제거한 고유 수험번호 수: " + uniqueCount);
        System.out.println("실패한 요청 수: " + failedUserIds.size());
        System.out.println("=========================================");

        assertThat(uniqueCount).isEqualTo(issuedExamineeNumbers.size());
    }

    @Test
    @DisplayName("원서가 없으면 임시저장 시 새로 생성되고 major는 null이어도 저장된다.")
    void saveDraft_major_null_저장가능() {
        //given
        User user = User.builder()
                .email("nullmajor@email.com")
                .password("pw")
                .name("student_null")
                .phone("010-9999-9999")
                .role(Role.STUDENT)
                .build();
        Long userId = userRepository.save(user).getId();

        AttendanceRequest attendanceRequest = new AttendanceRequest(0, 0, 0);
        SubjectScoreRequest subjectScoreRequest = new SubjectScoreRequest(Semester.SEMESTER_3_1, Subject.MATH, Grade.A);
        ScoreRequest scoreRequest = new ScoreRequest(List.of(subjectScoreRequest));
        ApplicationSaveRequest request = new ApplicationSaveRequest(null, attendanceRequest, scoreRequest);

        //when & then
        applicationService.saveDraft(userId, request);
    }
}