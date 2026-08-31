package com.admission.portal.domain.user.service;

import com.admission.portal.domain.user.dto.request.SignupRequest;
import com.admission.portal.domain.user.entity.Role;
import com.admission.portal.domain.user.entity.User;
import com.admission.portal.domain.user.repository.UserRepository;
import com.admission.portal.global.error.BusinessException;
import com.admission.portal.global.error.ErrorCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown(){
        userRepository.deleteAll();
    }

    private SignupRequest buildRequest(String email, String phone) {
        return new SignupRequest(email, "Password123!", "테스트", phone);
    }

    @Test
    @DisplayName("정상적인 요청이면 회원가입에 성공하고, role은 STUDENT로 고정되며 비밀번호는 암호화되어 저장된다")
    void signup_success() {
        //given
        SignupRequest request = buildRequest("test@test.com", "010-1111-1111");

        //when
        userService.signup(request);

        //then
        User savedUser = userRepository.findByEmail("test@test.com").orElseThrow();
        assertThat(savedUser.getRole()).isEqualTo(Role.STUDENT);
        assertThat(savedUser.getPassword()).isNotEqualTo("Password123!");
        assertThat(passwordEncoder.matches("Password123!", savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("이미 가입된 이메일로 회원가입하면 예외가 발생한다.")
    void signup_fail_duplicate_email() {
        //given
        SignupRequest request = buildRequest("test@test.com", "010-1111-1111");
        userService.signup(request);

        //when & then
        assertThatThrownBy(() -> userService.signup(buildRequest("test@test.com", "010-2222-2222")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_EMAIL.getMessage());


    }

    @Test
    @DisplayName("이미 가입된 연락처로 회원가입하면 예외가 발생한다.")
    void signup_fail_duplicate_phone() {
        //given
        userService.signup(buildRequest("first@test.com", "010-4444-4444"));

        //when & then
        assertThatThrownBy(() -> userService.signup(buildRequest("second@test.com", "010-4444-4444")))
                .isInstanceOf(BusinessException.class)
                .hasMessage(ErrorCode.DUPLICATE_PHONE.getMessage());
    }
}