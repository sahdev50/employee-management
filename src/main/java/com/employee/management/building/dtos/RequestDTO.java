package com.employee.management.building.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestDTO {

    @NotBlank(message = "Building name is required")
    @Size(min = 2, max = 100, message = "Building name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "A building must be assigned to a physical location (address)")
    private Long locationId;
}
