package com.employee.management.employee;

import com.employee.management.employee.dtos.RequestDTO;
import com.employee.management.employee.dtos.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // --- STANDARD CRUD OPERATIONS ---

    @GetMapping
    public ResponseEntity<List<ResponseDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createEmployee(
            @Valid @RequestBody RequestDTO requestDTO) {

        ResponseDTO created = employeeService.createEmployee(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody RequestDTO requestDTO) {

        ResponseDTO updated = employeeService.updateEmployee(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }


    // --- TEAM MANAGEMENT (Many-to-Many) ---

    @PostMapping("/{employeeId}/teams/{teamId}")
    public ResponseEntity<ResponseDTO> assignToTeam(
            @PathVariable Long employeeId,
            @PathVariable Long teamId) {

        ResponseDTO updatedEmployee = employeeService.assignEmployeeToTeam(employeeId, teamId);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/{employeeId}/teams/{teamId}")
    public ResponseEntity<ResponseDTO> removeFromTeam(
            @PathVariable Long employeeId,
            @PathVariable Long teamId) {

        ResponseDTO updatedEmployee = employeeService.removeEmployeeFromTeam(employeeId, teamId);
        return ResponseEntity.ok(updatedEmployee);
    }
}