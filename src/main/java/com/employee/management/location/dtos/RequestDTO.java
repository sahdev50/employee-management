package com.employee.management.location.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestDTO {

    @NotBlank(message = "Street address is required")
    @Size(min = 5, max = 255, message = "Street address must be between 5 and 255 characters")
    private String streetAddress;

    @NotBlank(message = "Postal code is required")
    @Size(min = 2, max = 20, message = "Postal code must be between 2 and 20 characters")
    private String postalCode;

    @NotNull(message = "A location must belong to a city")
    private Long cityId;
}
