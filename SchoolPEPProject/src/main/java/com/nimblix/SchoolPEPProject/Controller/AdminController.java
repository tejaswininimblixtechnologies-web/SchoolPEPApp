package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Request.AdminAccountCreateRequest;
import com.nimblix.SchoolPEPProject.Response.AdminProfileResponse;
import com.nimblix.SchoolPEPProject.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    public final AdminService adminService;

    @PostMapping("/adminlogin")
    public ResponseEntity<String> submitEmail(@RequestBody String email) {
        String response = adminService.submitEmail(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/adminregister")
    public ResponseEntity<Map<String, String>> createAdminAccount(@RequestBody AdminAccountCreateRequest request) {
        Map<String, String> response = new HashMap<>();

        try {
            Long adminId = adminService.createAdminAccount(request);
            response.put(SchoolConstants.MESSAGE, "Admin account created successfully. Admin ID: " + adminId);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put(SchoolConstants.MESSAGE, "Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("message", "Error: Something went wrong. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/studentlist")
    public ResponseEntity<?> getStudentList(
            @RequestParam Long schoolId,
            @RequestParam(required = false) Long classId,
            @RequestParam(required = false) String section,
            @RequestParam(required = false) String status
    ) {
        try {
            List<Student> students = adminService.getStudentList(
                    schoolId,
                    classId,
                    section,
                    status
            );

            if (students == null || students.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put(SchoolConstants.MESSAGE, "No students found for the given filters");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            Map<String, Object> success = new HashMap<>();
            success.put(SchoolConstants.MESSAGE, "Students fetched successfully");
            success.put(SchoolConstants.DATA, students);
            return ResponseEntity.ok(success);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(SchoolConstants.MESSAGE, "Failed to fetch student list: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @GetMapping("/profile")
    public AdminProfileResponse getAdminProfile(
            @RequestParam Long adminId,
            @RequestParam Long schoolId
    ) {
        return adminService.getAdminProfile(adminId, schoolId);
    }

    }



