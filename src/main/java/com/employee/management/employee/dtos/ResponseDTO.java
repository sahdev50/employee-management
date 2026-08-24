package com.employee.management.employee.dtos;

import com.employee.management.entity.EmployeeRole;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private EmployeeRole role;

    private Long departmentId;
    private Set<Long> teamIds;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}