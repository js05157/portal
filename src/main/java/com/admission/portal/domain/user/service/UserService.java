package com.admission.portal.domain.user.service;

import com.admission.portal.domain.user.dto.request.SignupRequest;
import com.admission.portal.domain.user.entity.Role;
import com.admission.portal.domain.user.entity.User;
import com.admission.portal.domain.user.repository.UserRepository;
import com.admission.portal.global.error.BusinessException;
import com.admission.portal.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        if(userRepository.existsByPhone(request.phone())) {
            throw new BusinessException(ErrorCode.DUPLICATE_PHONE);
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .name(request.name())
                .phone(request.phone())
                .role(Role.STUDENT)
                .build();

        userRepository.save(user);
    }

}
