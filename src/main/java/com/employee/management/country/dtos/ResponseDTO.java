package com.employee.management.country.dtos;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponseDTO {

    private Long id;
    private String name;
    private String isoCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
