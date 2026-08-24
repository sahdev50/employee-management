package com.employee.management.department;

import com.employee.management.department.dtos.RequestDTO;
import com.employee.management.department.dtos.ResponseDTO;

import java.util.List;

public interface DepartmentService {
    List<ResponseDTO> getAllDepartments();
    ResponseDTO getDepartmentById(Long id);
    ResponseDTO createDepartment(RequestDTO dto);
    ResponseDTO updateDepartment(Long id, RequestDTO dto); // Added for editing
    void deleteDepartment(Long id);
}
