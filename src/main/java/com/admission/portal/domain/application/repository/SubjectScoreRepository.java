package com.admission.portal.domain.application.repository;

import com.admission.portal.domain.application.entity.SubjectScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectScoreRepository extends JpaRepository<SubjectScore, Long> {
    List<SubjectScore> findByScoreId(Long scoreId);
}
