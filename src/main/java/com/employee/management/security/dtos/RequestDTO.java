package com.employee.management.security.dtos;

import com.employee.management.entity.EmployeeRole;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RequestDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    private String username;


    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()_\\-]).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character"
    )
    private String password;

    @NotNull(message = "Employee role must be specified")
    private EmployeeRole role;
}
