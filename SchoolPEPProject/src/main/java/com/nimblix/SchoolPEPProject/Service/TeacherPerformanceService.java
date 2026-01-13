package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Response.*;

import java.util.List;

public interface TeacherPerformanceService {
    StudentPerformanceSummaryResponse getSummary(
            Long studentId, Long schoolId, Long classId);

    List<SubjectAcademicResponse> getAcademicPerformance(Long studentId);

    AttendanceWeeklyResponse getAttendancePerformance(Long studentId);

    AssignmentAnalysisResponse getAssignmentPerformance(Long studentId);

    StudentDashboardResponse getDashboard(
            Long studentId, Long schoolId, Long classId);


}
