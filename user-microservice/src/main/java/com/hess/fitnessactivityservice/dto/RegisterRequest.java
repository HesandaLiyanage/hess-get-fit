package com.hess.fitnessactivityservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "password is required")
    @Size(min = 6 , message = "must have atleast 6 characters")
    private String password;

    @NotBlank(message = "email is required")
    private String email;

    private String firstName;
    private String lastName;


}
