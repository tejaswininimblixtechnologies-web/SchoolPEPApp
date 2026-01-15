package com.nimblix.SchoolPEPProject.ServiceImpl;

import com.nimblix.SchoolPEPProject.Model.Parent;
import com.nimblix.SchoolPEPProject.Repository.ParentRepository;
import com.nimblix.SchoolPEPProject.Request.ParentRegisterRequest;
import com.nimblix.SchoolPEPProject.Response.AuthParentResponse;
import com.nimblix.SchoolPEPProject.Service.ParentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {

    private final ParentRepository parentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthParentResponse signUp(ParentRegisterRequest request) {


        if (parentRepository.existsByEmailId(request.getEmailId())) {
            return new AuthParentResponse(false, "Email already registered", null);
        }
        Parent parent = new Parent();
        parent.setFirstName(request.getFullName());
        parent.setEmailId(request.getEmailId());
        parent.setMobile(request.getContactNumber());
        parent.setAddress(request.getAddress());
        parent.setSchoolId(request.getSchoolId());
        parent.setRole(request.getRole());


        // Encrypt password
        parent.setPassword(passwordEncoder.encode(request.getPassword()));

        parentRepository.save(parent);


        return new AuthParentResponse(true, "Parent registered successfully", null);
    }

}
