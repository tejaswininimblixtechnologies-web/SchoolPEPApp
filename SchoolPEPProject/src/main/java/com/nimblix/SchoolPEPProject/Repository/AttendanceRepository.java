package com.nimblix.SchoolPEPProject.Repository;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByStudentIdAndAttendanceDate(String studentId, String attendanceDate);
}
