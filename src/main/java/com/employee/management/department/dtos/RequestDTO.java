package com.employee.management.department.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestDTO {

    @NotBlank(message = "Department name is required and cannot be empty")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    // Optional: A department might not be tied to a physical building yet
    private Long buildingId;
}
