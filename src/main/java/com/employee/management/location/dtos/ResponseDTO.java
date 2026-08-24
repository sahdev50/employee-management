package com.employee.management.location.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponseDTO {

    private Long id;
    private String streetAddress;
    private String postalCode;
    private Long cityId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
