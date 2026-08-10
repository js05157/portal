package com.admission.portal.domain.application.entity;

import com.admission.portal.domain.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ApplicationTest {

    @Test
    @DisplayName("원서를 최종 제출하면 상태가 SUBMITTED로 변경되어 제출 시간이 기록된다.")
    void submit_success() {
        //given
        User mockUser = mock(User.class);
        Application application = Application.builder()
                .user(mockUser)
                .major("소프트웨어과")
                .status(ApplicationStatus.DRAFT)
                .build();

        //when
        application.submit("SW-0001");

        //then
        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED);
        assertThat(application.getSubmittedAt()).isNotNull();
    }

    @Test
    @DisplayName("이미 제출된 원서를 다시 제출하면 예외가 발생한다.")
    void submit_fail_already_submitted() {
        //given
        User mockUser = mock(User.class);
        Application application = Application.builder()
                .user(mockUser)
                .major("소프트웨어과")
                .status(ApplicationStatus.SUBMITTED)
                .build();

        //when & then
        assertThatThrownBy(() -> application.submit("SW-0001"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 최종 제출된 원서입니다.");
    }
}