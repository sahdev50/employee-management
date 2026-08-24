package com.employee.management.employee;

import com.employee.management.employee.dtos.RequestDTO;
import com.employee.management.employee.dtos.ResponseDTO;

import java.util.List;

public interface EmployeeService {
    List<ResponseDTO> getAllEmployees();
    ResponseDTO getEmployeeById(Long id);
    ResponseDTO createEmployee(RequestDTO employeeDTO);
    void deleteEmployee(Long id);
    ResponseDTO assignEmployeeToTeam(Long employeeId, Long teamId);
    ResponseDTO removeEmployeeFromTeam(Long employeeId, Long teamId);
    ResponseDTO updateEmployee(Long id, RequestDTO dto);
}
