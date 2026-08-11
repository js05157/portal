package com.admission.portal.domain.application.repository;

import com.admission.portal.domain.application.entity.AdmissionResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdmissionResultRepository extends JpaRepository<AdmissionResult, Long> {
    Optional<AdmissionResult> findByApplicationId(Long applicationId);
}
