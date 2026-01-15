package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Repository.AttendanceRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    @PreAuthorize("hasRole('TEACHER')")
    @Override
    public void markAttendance(String studentId,String attendanceDate, String attendanceStatus) {

        Student student = studentRepository.findByStudentId(studentId);
        if(student == null){
            throw new RuntimeException("Student Not Found");
        }

        if(attendanceRepository.existsByStudentIdAndAttendanceDate(studentId, attendanceDate)){
            throw new RuntimeException("Attendance already marked for this date");
        }

        Attendance attendance = new Attendance();
        attendance.setStudentId(studentId);
        attendance.setAttendanceDate(attendanceDate);
        attendance.setAttendanceStatus(attendanceStatus);

        attendanceRepository.save(attendance);
    }
}
