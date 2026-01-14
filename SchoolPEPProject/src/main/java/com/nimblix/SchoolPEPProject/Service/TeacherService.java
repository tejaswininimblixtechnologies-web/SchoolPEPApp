package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface TeacherService {
    Map<String, String> registerTeacher(TeacherRegistrationRequest request);

//    ResponseEntity<Teacher> getTeacherDetails(Long teacherId);

    ResponseEntity<Map<String, String>> createClassroom(ClassroomRequest request);

    public TeacherDetailsResponse getTeacherDetails(Long teacherId);

    Map<String, String> updateTeacherDetails(TeacherRegistrationRequest request, Long teacherId);

    Map<String, String> deleteTeacherDetails(Long teacherId, Long schoolId);

    Map<String, String> createAssignment(CreateAssignmentRequest request, MultipartFile[] files);

    Map<String, String> onboardSubject(OnboardSubjectRequest request);

    Map<String, String> updateAssignment(CreateAssignmentRequest request, MultipartFile[] files);

    Map<String, String> updateOnboardSubject(OnboardSubjectRequest request);

    Map<String, String> deleteAssignment(Long assignmentId, Long subjectId);

        // Performance APIs
        Map<String, Object> getStudentPerformanceSummary(Long studentId, Long schoolId, Long classId);
        Map<String, Object> getAcademicPerformanceGraph(Long studentId);
        Map<String, Object> getAttendancePerformanceGraph(Long studentId, String week, String month);
        Map<String, Object> getAssignmentCompletionAnalysis(Long studentId);
        Map<String, Object> getPerformanceDashboard(Long studentId);

}
