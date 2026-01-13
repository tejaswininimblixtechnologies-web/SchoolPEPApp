package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);

    long countByStudentId(Long studentId);

    long countByStudentIdAndAttendanceStatus(
            Long studentId,
            String attendanceStatus
    );



}
