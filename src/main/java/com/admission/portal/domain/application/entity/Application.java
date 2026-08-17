package com.admission.portal.domain.application.entity;

import com.admission.portal.domain.user.entity.User;
import com.admission.portal.global.common.entitiy.BaseTimeEntity;
import com.admission.portal.global.error.BusinessException;
import com.admission.portal.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "application")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(unique = true, length = 20)
    private String examineeNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private Major major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    private LocalDateTime submittedAt;

    @Builder
    public Application(User user, ApplicationStatus status) {
        this.user = user;
        this.status = status != null ? status : ApplicationStatus.DRAFT;
    }

    public void submit(String examineeNumber) {
        if(this.status == ApplicationStatus.SUBMITTED){
            throw new BusinessException(ErrorCode.ALREADY_SUBMITTED);
        }
        this.examineeNumber = examineeNumber;
        this.status = ApplicationStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    public void updateMajor(Major major) {
        this.major = major;
    }
}
