package com.employee.management.country.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestDTO {

    @NotBlank(message = "Country name is required")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "ISO code is required")
    @Size(min = 2, max = 2, message = "ISO code must be exactly 2 characters (e.g., US, IE, IN)")
    private String isoCode;
}