package com.admission.portal.domain.application.repository;

import com.admission.portal.domain.application.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByApplicationId(Long applicationId);
}
