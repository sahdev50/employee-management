package com.employee.management.department.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponseDTO {

    private Long id;
    private String name; // Changed from Enum to String
    private Long buildingId;

    // Auditing fields inherited from BaseEntity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
