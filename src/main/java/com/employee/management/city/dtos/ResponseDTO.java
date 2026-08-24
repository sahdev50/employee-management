package com.employee.management.city.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponseDTO {

    private Long id;
    private String name;
    private Long countryId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
