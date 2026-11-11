package com.employee.management.security.dtos;

import com.employee.management.entity.EmployeeRole;
import lombok.Data;

@Data
public class ResponseDTO {
    private Long id;
    private String username;
    private String email;
    private EmployeeRole role;
    private boolean isEnable;
}
