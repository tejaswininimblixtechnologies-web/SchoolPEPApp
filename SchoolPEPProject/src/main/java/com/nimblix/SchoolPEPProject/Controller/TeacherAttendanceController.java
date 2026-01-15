package com.nimblix.SchoolPEPProject.Controller;


import com.nimblix.SchoolPEPProject.Service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/teacher/attendance")
public class TeacherAttendanceController {

    private final AttendanceService attendanceService;
    @PostMapping("/mark")
    public ResponseEntity<String> markAttendance(
            @RequestParam String studentId,
            @RequestParam String attendanceDate,
            @RequestParam String attendanceStatus
    ){
        attendanceService.markAttendance(studentId, attendanceDate, attendanceStatus);
        return ResponseEntity.ok("Attendance marked successfully");
    }
}
