package com.example.bnyan.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectManagerDTO {

    @NotEmpty(message = "username must be filled")
    private String username;

    @NotEmpty(message = "password must be filled")
    private String password;

    @NotEmpty(message = "email must be filled")
    @Email(message = "email must be valid")
    private String email;

    @NotEmpty(message = "phoneNumber must be filled")
    @Pattern(regexp = "^05\\d{8}$", message = "phoneNumber must start with 05 and be 10 digits")
    private String phoneNumber;
}
