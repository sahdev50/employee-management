package com.employee.management.team.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestDTO {

    @NotBlank(message = "Team name is required and cannot be empty")
    @Size(min = 2, max = 100, message = "Team name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "A team must be assigned to a department")
    private Long departmentId;
}
