package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.OtpVerifyRequest;
import com.nimblix.SchoolPEPProject.Request.SchoolRegistrationRequest;
import com.nimblix.SchoolPEPProject.Request.SubscriptionRequest;
import com.nimblix.SchoolPEPProject.Response.SchoolListResponse;
import com.nimblix.SchoolPEPProject.Service.SchoolService;
import com.nimblix.SchoolPEPProject.Util.SchoolUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/school")
@RestController
@RequiredArgsConstructor
public class SchoolController {
    private final SchoolService schoolService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> registerSchool(
            @Valid @RequestBody SchoolRegistrationRequest request) {

        School school = schoolService.registerSchool(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 201);
        response.put("message", "School registered successfully");
        response.put("schoolId", school.getSchoolId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @PostMapping("/resendOtp")
    public ResponseEntity<Map<String, Object>> resendOtp(
            @RequestParam String email) {


        schoolService.resendSchoolOtp(email);

        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        SchoolConstants.MESSAGE, "OTP resent successfully"
                )
        );
    }


    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listSchools() {
        List<SchoolListResponse> schools = schoolService.getAllSchools();
        Map<String, Object> response = new HashMap<>();
        response.put(SchoolConstants.SCHOOLS_LIST, schools);
        return ResponseEntity.ok(response);
    }



    @PostMapping("/verifyOtp")
    public ResponseEntity<Map<String, Object>> verifyOtp(
            @RequestBody OtpVerifyRequest request) {

        schoolService.verifySchoolOtp(
                request.getEmail(),
                request.getOtp()
        );

        return ResponseEntity.ok(
                Map.of(
                        SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                        SchoolConstants.MESSAGE, "OTP verified successfully"
                )
        );
    }


    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(
            @RequestBody SubscriptionRequest request) {

        School school = schoolService.getLoggedInSchool();

        schoolService.activatePaidSubscription(school, request);

        return ResponseEntity.ok(Map.of(
                SchoolConstants.STATUS_SUCCESS, "SUCCESS",
                SchoolConstants.MESSAGE, "Subscription activated"
        ));
    }


    @GetMapping("/subscription")
    public ResponseEntity<?> status() {

        School school = schoolService.getLoggedInSchool();

        schoolService.validateSubscription(school);

        return ResponseEntity.ok(Map.of(
                SchoolConstants.SUBSCRIPTION_STATUS, school.getSubscriptionStatus(),
                "trialEndDate", school.getTrialEndDate()
        ));
    }
}
