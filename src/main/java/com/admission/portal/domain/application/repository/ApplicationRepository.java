package com.admission.portal.domain.application.repository;

import com.admission.portal.domain.application.entity.Application;
import com.admission.portal.domain.application.entity.ApplicationStatus;
import com.admission.portal.domain.application.entity.Major;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    boolean existsByExamineeNumber(String examineeNumber);
    long countByMajorAndStatus(Major major, ApplicationStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    @Query("SELECT COUNT(a) FROM Application a WHERE a.major = :major AND a.status = :status")
    long countByMajorAndStatusForUpdate(@Param("major") Major major, @Param("status") ApplicationStatus status);
}
