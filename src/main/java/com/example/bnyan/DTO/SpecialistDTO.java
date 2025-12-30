package com.example.bnyan.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialistDTO {

    @NotEmpty(message = "username must be filled")
    private String username;

    @NotEmpty(message = "password must be filled")
    private String password;

    // user name like Ahmed Ali
    @NotEmpty(message = "username must be filled")
    @Pattern(regexp = "^[A-Za-z]{3,20}( [A-Za-z]{3,20})+$",
            message = "fullName must contain only letters and a space between first and last name")
    private String fullName;

    @NotEmpty(message = "email must be filled")
    @Email(message = "email must be valid")
    private String email;

    @NotEmpty(message = "phoneNumber must be filled")
    @Pattern(regexp = "^05\\d{8}$", message = "phoneNumber must start with 05 and be 10 digits")
    private String phoneNumber;

    @Pattern(regexp = "^(PROJECT_MANAGER|DESIGNER|MECHANICAL_ENGINEER|CIVIL_ENGINEER|ARCHITECTURE_ENGINEER|CONTRACTOR)$",message = "specialty must be: PROJECT_MANAGER, DESIGNER, MECHANICAL_ENGINEER, CIVIL_ENGINEER, ARCHITECTURE_ENGINEER, or CONTRACTOR")
    private String speciality;

}
