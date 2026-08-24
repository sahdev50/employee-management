package com.employee.management.building.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponseDTO {

    private Long id;
    private String name;
    private Long locationId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}