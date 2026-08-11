package com.admission.portal.domain.application.entity;

import com.admission.portal.global.common.entitiy.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admission_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class AdmissionResult extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private Application application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdmissionStatus status;

    private Integer rankOrder;

    private Integer waitingNumber;

    @Builder
    public AdmissionResult(Application application, AdmissionStatus status, Integer rankOrder, Integer waitingNumber){
        this.application = application;
        this.status = status;
        this.rankOrder = rankOrder;
        this.waitingNumber = waitingNumber;
    }

    public void updateResult(AdmissionStatus status, Integer rankOrder, Integer waitingNumber) {
        this.status = status;
        this.rankOrder = rankOrder;
        this.waitingNumber = waitingNumber;
    }
}
