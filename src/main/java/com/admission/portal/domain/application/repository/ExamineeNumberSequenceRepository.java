package com.admission.portal.domain.application.repository;

import com.admission.portal.domain.application.entity.ExamineeNumberSequence;
import com.admission.portal.domain.application.entity.Major;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExamineeNumberSequenceRepository extends JpaRepository<ExamineeNumberSequence, Major> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM ExamineeNumberSequence s WHERE s.major = :major")
    Optional<ExamineeNumberSequence> findByMajorForUpdate(@Param("major") Major major);
}
