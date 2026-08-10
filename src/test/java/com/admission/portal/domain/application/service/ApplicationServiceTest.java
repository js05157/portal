package com.admission.portal.domain.application.service;

import com.admission.portal.domain.application.entity.Application;
import com.admission.portal.domain.application.entity.ApplicationStatus;
import com.admission.portal.domain.application.repository.ApplicationRepository;
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
    private ApplicationService applicationService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private UserRepository userRepository;

    private List<Long> applicationIds = new ArrayList<>();

    @BeforeEach
    void setUp(){
        for(int i = 0; i < 100; i++) {
            User user = User.builder()
                    .email("test" + i + "@email.com")
                    .password("pw")
                    .name("student" + i)
                    .phone("010-0000-" + String.format("%04d", i))
                    .role(Role.STUDENT)
                    .build();

            userRepository.save(user);

            Application application = Application.builder()
                    .user(user)
                    .major("SW")
                    .status(ApplicationStatus.DRAFT)
                    .build();

            Application savedApp = applicationRepository.save(application);
            applicationIds.add(savedApp.getId());
        }
    }

    @AfterEach
    void tearDown() {
        applicationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("동시성 이슈 발생: 100명이 동시에 제출하면 수험번호가 중복 발급된다.")
    void submitApplicationV1() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<String> issuedExamineeNumbers = java.util.Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < threadCount; i++) {
            Long appId = applicationIds.get(i);
            executorService.submit(() -> {
                try {
                    String examineeNum = applicationService.submitApplicationV1(appId);
                    issuedExamineeNumbers.add(examineeNum);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long uniqueNumbersCount = issuedExamineeNumbers.stream().distinct().count();

        System.out.println("=========================================");
        System.out.println("발급된 전체 수험번호 수: " + issuedExamineeNumbers.size());
        System.out.println("중복을 제거한 고유 수험번호 수: " + uniqueNumbersCount);
        System.out.println("=========================================");

        assertThat(uniqueNumbersCount).isLessThan(100);
    }
}