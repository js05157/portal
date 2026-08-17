package com.admission.portal.domain.application.repository;

import com.admission.portal.domain.application.entity.Application;
import com.admission.portal.domain.application.entity.ApplicationStatus;
import com.admission.portal.domain.application.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    boolean existsByExamineeNumber(String examineeNumber);
    long countByMajorAndStatus(Major major, ApplicationStatus status);
}
