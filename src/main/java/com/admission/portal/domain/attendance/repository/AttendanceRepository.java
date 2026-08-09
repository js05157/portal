package com.admission.portal.domain.attendance.repository;

import com.admission.portal.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByApplicationId(Long applicationId);
}
