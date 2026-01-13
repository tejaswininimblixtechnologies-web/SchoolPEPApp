package com.nimblix.SchoolPEPProject.Controller;

import com.nimblix.SchoolPEPProject.Constants.SchoolConstants;
import com.nimblix.SchoolPEPProject.Model.User;
import com.nimblix.SchoolPEPProject.Repository.UserRepository;
import com.nimblix.SchoolPEPProject.Request.AuthStudentRequest;
import com.nimblix.SchoolPEPProject.Response.AuthStudentResponse;
import com.nimblix.SchoolPEPProject.Security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/encode")
    public String encode() {
        return passwordEncoder.encode("password123");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthStudentRequest request) {

        try {
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap(SchoolConstants.MESSAGE, "Email is required."));
            }

            // Fetch from DB
            User user = userRepository
                    .findByEmailId(request.getEmail())
                    .filter(u -> u.getStatus().equalsIgnoreCase(SchoolConstants.STATUS_ACTIVE))
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "User not found or inactive."));
            }

            String role = user.getRole().getRoleName().toUpperCase();

            if (role.equals(SchoolConstants.STUDENT)) {

                if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(Collections.singletonMap("message", "Password is required for Student login."));
                }

                // Authenticate
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
            }

            else {
                // No password check — allow login by email only
                System.out.println(role + " logged in using email only");
            }

            // Generate JWT token
            var userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtUtil.generateToken(userDetails);

            // Update login status
            user.setIsLogin(true);
            userRepository.save(user);

            // Build response
            AuthStudentResponse resp = new AuthStudentResponse();
            resp.setUserId(user.getId());
            resp.setFirstName(user.getFirstName());
            resp.setLastName(user.getLastName());
            resp.setEmail(user.getEmailId());
            resp.setRole(role);
            resp.setToken(token);

            return ResponseEntity.ok(resp);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", "Incorrect password."));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Login failed."));
        }
    }

    @PostMapping("/teacher/login")
    public ResponseEntity<?> teacherLogin(@RequestBody AuthStudentRequest request) {
        try{
            if(request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE,
                                SchoolConstants.MESSAGE, "Email is required"
                        ));
            }
            if(request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of(
                                SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE,
                                SchoolConstants.MESSAGE, "Password is required"
                        ));
            }

            User user=userRepository.findByEmailId(request.getEmail())
                    .filter(u -> SchoolConstants.STATUS_ACTIVE.equalsIgnoreCase(u.getStatus()))
                    .orElse(null);

            if(user == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE,
                                SchoolConstants.MESSAGE, "Teacher not found or inactive"
                        ));
            }

            if(!SchoolConstants.TEACHER_ROLE.equalsIgnoreCase(user.getRole().getRoleName())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE,
                                SchoolConstants.MESSAGE, "Not a teacher account"
                        ));
            }
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var userDetails=userDetailsService.loadUserByUsername(request.getEmail());
            String token=jwtUtil.generateToken(userDetails);

            AuthStudentResponse data=new AuthStudentResponse();
            data.setUserId(user.getId());
            data.setFirstName(user.getFirstName());
            data.setLastName(user.getLastName());
            data.setEmail(user.getEmailId());
            data.setRole(user.getRole().getRoleName());
            data.setToken(token);

            return ResponseEntity.ok(Map.of(
                    SchoolConstants.STATUS, SchoolConstants.STATUS_SUCCESS,
                    SchoolConstants.MESSAGE, "Login successful",
                    SchoolConstants.DATA, data
            ));
        } catch(BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE,
                            SchoolConstants.MESSAGE, "Invalid password"
                    ));
        } catch(Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            SchoolConstants.STATUS, SchoolConstants.STATUS_FAILURE,
                            SchoolConstants.MESSAGE, "Teacher login failed"
                    ));
        }
    }
}
