package com.employee.management.security.service;

import com.employee.management.security.dtos.RequestDTO;
import com.employee.management.security.dtos.ResponseDTO;

public interface UserService {
    ResponseDTO signUp(RequestDTO dto);
}
