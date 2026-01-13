package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Assignments;
import com.nimblix.SchoolPEPProject.Model.Attendance;
import com.nimblix.SchoolPEPProject.Repository.AssignmentsRepository;
import com.nimblix.SchoolPEPProject.Repository.AttendanceRepository;
import com.nimblix.SchoolPEPProject.Service.TeacherPerformanceService;
import com.nimblix.SchoolPEPProject.Response.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherPerformanceServiceImpl
        implements TeacherPerformanceService {

    private final AssignmentsRepository assignmentsRepository;
    private final AttendanceRepository attendanceRepository;


    @Override
    public StudentPerformanceSummaryResponse getSummary(
            Long studentId, Long schoolId, Long classId) {

        long totalAssignments =
                assignmentsRepository.findByAssignedToUserId(studentId).size();

        long submitted =
                assignmentsRepository.countByAssignedToUserIdAndStatus(
                        studentId, "SUBMITTED"
                );

        long totalAttendance =
                attendanceRepository.countByStudentId(studentId);

        long present =
                attendanceRepository.countByStudentIdAndAttendanceStatus(
                        studentId, "PRESENT"
                );

        StudentPerformanceSummaryResponse response =
                new StudentPerformanceSummaryResponse();

        response.setAssignmentCompletionPercentage(
                totalAssignments == 0 ? 0 :
                        (submitted * 100.0) / totalAssignments
        );

        response.setAttendancePercentage(
                totalAttendance == 0 ? 0 :
                        (present * 100.0) / totalAttendance
        );

        // Academic score derived from assignment completion
        response.setAcademicScore(
                response.getAssignmentCompletionPercentage()
        );

        return response;
    }


    @Override
    public List<SubjectAcademicResponse> getAcademicPerformance(
            Long studentId) {

        List<Assignments> list =
                assignmentsRepository.findByAssignedToUserId(studentId);

        return list.stream()
                .collect(Collectors.groupingBy(
                        Assignments::getSubjectId
                ))
                .entrySet()
                .stream()
                .map(e -> {
                    long total = e.getValue().size();
                    long submitted = e.getValue().stream()
                            .filter(a -> "SUBMITTED".equals(a.getStatus()))
                            .count();

                    double avg = total == 0 ? 0 :
                            (submitted * 100.0) / total;

                    return new SubjectAcademicResponse(
                            e.getKey(), avg
                    );
                })
                .toList();
    }


    @Override
    public AttendanceWeeklyResponse getAttendancePerformance(
            Long studentId) {

        List<Attendance> records =
                attendanceRepository.findByStudentId(studentId);

        List<AttendanceWeeklyResponse.DayAttendance> days =
                records.stream()
                        .map(a -> new AttendanceWeeklyResponse.DayAttendance(
                                a.getAttendanceDate(),
                                "PRESENT".equals(a.getAttendanceStatus())
                        ))
                        .toList();

        AttendanceWeeklyResponse response =
                new AttendanceWeeklyResponse();

        response.setWeek("CURRENT_WEEK");
        response.setAttendance(days);

        return response;
    }


    @Override
    public AssignmentAnalysisResponse getAssignmentPerformance(
            Long studentId) {

        List<Assignments> list =
                assignmentsRepository.findByAssignedToUserId(studentId);

        List<AssignmentAnalysisResponse.AssignmentStatus> statuses =
                list.stream()
                        .map(a ->
                                new AssignmentAnalysisResponse.AssignmentStatus(
                                        a.getSubjectId(),
                                        "SUBMITTED".equals(a.getStatus()),
                                        "PENDING".equals(a.getStatus()),
                                        "LATE".equals(a.getStatus())
                                )
                        ).toList();

        long submitted =
                list.stream()
                        .filter(a -> "SUBMITTED".equals(a.getStatus()))
                        .count();

        AssignmentAnalysisResponse response =
                new AssignmentAnalysisResponse();

        response.setAssignments(statuses);
        response.setCompletionPercentage(
                list.isEmpty() ? 0 :
                        (submitted * 100.0) / list.size()
        );

        return response;
    }


    @Override
    public StudentDashboardResponse getDashboard(
            Long studentId, Long schoolId, Long classId) {

        StudentDashboardResponse response =
                new StudentDashboardResponse();

        response.setSummary(
                getSummary(studentId, schoolId, classId)
        );
        response.setAcademics(
                getAcademicPerformance(studentId)
        );
        response.setAttendance(
                getAttendancePerformance(studentId)
        );
        response.setAssignments(
                getAssignmentPerformance(studentId)
        );

        return response;
    }
}
