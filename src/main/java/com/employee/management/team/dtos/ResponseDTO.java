package com.employee.management.team.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDTO {
    private Long id;
    private String name;
    private Long departmentId;

    // Auditing fields inherited from BaseEntity
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
