package com.nimblix.SchoolPEPProject.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolRegistrationRequest {

    @NotBlank(message = "School name is mandatory")
    private String schoolName;

    @NotBlank(message = "School email is mandatory")
    @Email(message = "Invalid email format")
    private String schoolEmail;

    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "Password must contain upper, lower, number, special character and be minimum 8 characters"
    )
    private String password;

    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;

    @NotBlank(message = "Mobile number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be exactly 10 digits")
    private String schoolPhone;

    @NotBlank(message = "School address is mandatory")
    private String schoolAddress;

    // Optional but accepted from frontend
    private Double latitude;
    private Double longitude;

    @NotBlank(message = "Location type is mandatory")
    private String locationType;
}
