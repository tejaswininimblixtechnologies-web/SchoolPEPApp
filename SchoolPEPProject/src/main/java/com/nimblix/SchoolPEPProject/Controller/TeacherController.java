package com.nimblix.SchoolPEPProject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Teacher;
import com.nimblix.SchoolPEPProject.Request.ClassroomRequest;
import com.nimblix.SchoolPEPProject.Request.CreateAssignmentRequest;
import com.nimblix.SchoolPEPProject.Request.OnboardSubjectRequest;
import com.nimblix.SchoolPEPProject.Request.TeacherRegistrationRequest;
import com.nimblix.SchoolPEPProject.Response.TeacherDetailsResponse;
import com.nimblix.SchoolPEPProject.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

        // 1️⃣ Get Student Performance Summary (Top Section)
        @GetMapping("/students/{studentId}/performance/summary")
        public ResponseEntity<Map<String, Object>> getStudentPerformanceSummary(
                        @PathVariable Long studentId,
                        @RequestParam Long schoolId,
                        @RequestParam Long classId) {
                Map<String, Object> summary = teacherService.getStudentPerformanceSummary(studentId, schoolId, classId);
                return ResponseEntity.ok(summary);
        }

        // 2️⃣ Academic Performance – Subject-wise Graph
        @GetMapping("/students/{studentId}/performance/academics")
        public ResponseEntity<Map<String, Object>> getAcademicPerformanceGraph(@PathVariable Long studentId) {
                Map<String, Object> graph = teacherService.getAcademicPerformanceGraph(studentId);
                return ResponseEntity.ok(graph);
        }

        // 3️⃣ Attendance Performance – Weekly Graph
        @GetMapping("/students/{studentId}/performance/attendance")
        public ResponseEntity<Map<String, Object>> getAttendancePerformanceGraph(
                        @PathVariable Long studentId,
                        @RequestParam(required = false) String week,
                        @RequestParam(required = false) String month) {
                Map<String, Object> attendance = teacherService.getAttendancePerformanceGraph(studentId, week, month);
                return ResponseEntity.ok(attendance);
        }

        // 4️⃣ Assignment Completion Analysis
        @GetMapping("/students/{studentId}/performance/assignments")
        public ResponseEntity<Map<String, Object>> getAssignmentCompletionAnalysis(@PathVariable Long studentId) {
                Map<String, Object> analysis = teacherService.getAssignmentCompletionAnalysis(studentId);
                return ResponseEntity.ok(analysis);
        }

        // 5️⃣ Combined Graph Analytics API (Optional)
        @GetMapping("/students/{studentId}/performance/dashboard")
        public ResponseEntity<Map<String, Object>> getPerformanceDashboard(@PathVariable Long studentId) {
                Map<String, Object> dashboard = teacherService.getPerformanceDashboard(studentId);
                return ResponseEntity.ok(dashboard);
        }
    @PostMapping("/teacherRegister")
    public Map<String, String> registerTeacher(@RequestBody TeacherRegistrationRequest request) {
        return teacherService.registerTeacher(request);
    }

    @GetMapping("/getTeacher")
    public ResponseEntity<TeacherDetailsResponse> getTeacherDetails(
            @RequestParam Long teacherId) {

        TeacherDetailsResponse response =
                teacherService.getTeacherDetails(teacherId);

        return ResponseEntity.ok(response);
    }


    @PutMapping(
            value = "/updateTeacher",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> updateTeacherDetails(
            @RequestBody TeacherRegistrationRequest request,
            @RequestParam Long teacherId) {

        return teacherService.updateTeacherDetails(request, teacherId);
    }



    @DeleteMapping("/delete")
    public  Map<String,String> deleteTeacherRecord(@RequestParam Long teacherId, @RequestParam Long schoolId){
        return  teacherService.deleteTeacherDetails(teacherId,schoolId);
    }


    @PostMapping("/createClassroom")
    public ResponseEntity<Map<String, String>> createClassroom(@RequestBody ClassroomRequest request) {
        return teacherService.createClassroom(request);
    }

    @PostMapping(
            value = "/createAssignment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> createAssignment(
            @RequestPart String assignmentJson,
            @RequestPart MultipartFile[] files
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateAssignmentRequest request =
                    objectMapper.readValue(assignmentJson, CreateAssignmentRequest.class);

            return ResponseEntity.ok(
                    teacherService.createAssignment(request, files)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS,SchoolConstants.STATUS_ERORR ,
                            SchoolConstants.MESSAGE, "Invalid assignment payload"
                    )
            );
        }
    }


    @PostMapping(
            value = "/updateAssignment",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Map<String, String>> updateAssignment(
            @RequestPart String assignmentJson,
            @RequestPart(required = false) MultipartFile[] files
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateAssignmentRequest request =
                    objectMapper.readValue(assignmentJson, CreateAssignmentRequest.class);

            return ResponseEntity.ok(
                    teacherService.updateAssignment(request, files)
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE, "Invalid assignment payload"
                    )
            );
        }
    }


    @PostMapping("/onboardSubject")
    public ResponseEntity<Map<String, String>> onboardSubject(
            @RequestBody OnboardSubjectRequest request
    ) {

        if (request == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE, "Request body is missing"
                    )
            );
        }

        return ResponseEntity.ok(teacherService.onboardSubject(request));
    }

    @PostMapping("/updateOnboardedSubject")
    public ResponseEntity<Map<String, String>> updateOnboardSubject(
            @RequestBody OnboardSubjectRequest request
    ) {

        if (request == null || request.getSubjectId() == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE, "Request body or Subject ID is missing"
                    )
            );
        }

        return ResponseEntity.ok(teacherService.updateOnboardSubject(request));
    }

    @PostMapping("/deleteAssignmentByIdAndSubjectId")
    public ResponseEntity<Map<String, String>> deleteAssignment(
            @RequestParam Long assignmentId,
            @RequestParam Long subjectId
    ) {
        if (assignmentId == null || subjectId == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_ERORR,
                            SchoolConstants.MESSAGE, "Assignment ID or Subject ID is missing"
                    )
            );
        }

        return ResponseEntity.ok(
                teacherService.deleteAssignment(assignmentId, subjectId)
        );
    }

}
