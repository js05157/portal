package com.admission.portal.domain.application.entity;

import com.admission.portal.domain.user.entity.User;
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
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(unique = true, length = 20)
    private String examineeNumber;

    @Column(nullable = false, length = 30)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    private LocalDateTime submittedAt;

    @Builder
    public Application(User user, String major, ApplicationStatus status) {
        this.user = user;
        this.major = major;
        this.status = status != null ? status : ApplicationStatus.DRAFT;
    }

    public void submit() {
        if(this.status == ApplicationStatus.SUBMITTED){
            // TODO: 추후 Service 계층 개발 시 BusinessException(ErrorCode.ALREADY_SUBMITTED)으로 교체
            throw new IllegalStateException("이미 최종 제출된 원서입니다.");
        }
        this.status = ApplicationStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }
}
