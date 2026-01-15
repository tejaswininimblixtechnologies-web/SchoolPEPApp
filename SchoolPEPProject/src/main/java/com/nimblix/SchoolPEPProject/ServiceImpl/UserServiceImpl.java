package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.Parent;
import com.nimblix.SchoolPEPProject.Model.Student;
import com.nimblix.SchoolPEPProject.Repository.ParentRepository;
import com.nimblix.SchoolPEPProject.Repository.StudentRepository;
import com.nimblix.SchoolPEPProject.Request.UserRegistrationRequest;
import com.nimblix.SchoolPEPProject.Service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final PasswordEncoder passwordEncoder;

//    @PostConstruct
//    public void generatePassword() {
//        System.out.println(
//                passwordEncoder.encode("password123")
//        );
//    }


    @Override
    public ResponseEntity<?> registerUser(UserRegistrationRequest request) {

        // 1️⃣ Password validation
        if (!request.getPassword().equals(request.getReEnterPassword())) {
            return ResponseEntity.badRequest()
                    .body("Password and Re-Enter Password do not match");
        }

        // 2️⃣ Email uniqueness
        if (studentRepository.existsByEmailId(request.getEmail()) ||
                parentRepository.existsByEmailId(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body("Email already registered");
        }

        // 3️⃣ Role-based logic
        if (SchoolConstants.STUDENT.equalsIgnoreCase(request.getRole())) {

            // Student ID mandatory
            if (request.getStudentId() == null || request.getStudentId().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("Student ID is required");
            }

            Student student = new Student();
            student.setStudentId(request.getStudentId());
            student.setFirstName(request.getFirstName());
            student.setLastName(request.getLastName());
            student.setEmailId(request.getEmail());
            student.setPassword(passwordEncoder.encode(request.getPassword()));
            student.setMobile(request.getMobileNumber()); // optional
            student.setSchoolId(request.getSchoolId());
            student.setStatus(SchoolConstants.ACTIVE);
            student.setIsLogin(Boolean.FALSE);
            student.setDesignation(SchoolConstants.STUDENT);

            studentRepository.save(student);

            return ResponseEntity.ok("Student registered successfully!");

        } else if (SchoolConstants.PARENT.equalsIgnoreCase(request.getRole())) {

            // Parent ID mandatory
            if (request.getParentId() == null || request.getParentId().isBlank()) {
                return ResponseEntity.badRequest()
                        .body("Parent ID is required");
            }

            Student student = studentRepository.findByStudentId(request.getStudentId());

            Parent parent = new Parent();
            parent.setParentId(request.getParentId());
            parent.setFirstName(request.getFirstName());
            parent.setLastName(request.getLastName());
            parent.setEmailId(request.getEmail());
            parent.setPassword(passwordEncoder.encode(request.getPassword()));
            parent.setMobile(request.getMobileNumber());
            parent.setStudents(List.of(student));
            parent.setStatus(SchoolConstants.ACTIVE);

            parentRepository.save(parent);

            return ResponseEntity.ok("Parent registered successfully!");
        }

        return ResponseEntity.badRequest().body("Invalid role");
    }

}
