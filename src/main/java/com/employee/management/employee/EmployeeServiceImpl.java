package com.employee.management.employee;

import com.employee.management.employee.dtos.RequestDTO;
import com.employee.management.employee.dtos.ResponseDTO;
import com.employee.management.entity.Department;
import com.employee.management.entity.Employee;
import com.employee.management.entity.EmployeeRole;
import com.employee.management.entity.Team;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.department.DepartmentRepository;
import com.employee.management.team.TeamRepository;
import com.employee.management.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found")
        );

        return employeeMapper.toResponseDto(employee);
    }

    @Override
    @Transactional
    public ResponseDTO createEmployee(RequestDTO employeeDTO) {
        if(employeeRepository.existsByEmail(employeeDTO.getEmail())){
            throw new BusinessRuleException("Email already in use : " + employeeDTO.getEmail());
        }

        if(employeeDTO.getRole() == EmployeeRole.JUNIOR && employeeDTO.getDepartmentId() == null){
            throw new BusinessRuleException("Junior employees must be assigned to a department upon creation.");
        }

        Employee employee = employeeMapper.toEntity(employeeDTO);

        if(employeeDTO.getDepartmentId() != null){
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(
                            ()-> new ResourceNotFoundException("Department not found")
                    );

            employee.setDepartment(department);
        }



        return employeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        // 1. Fetch the employee to ensure they exist
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // 2. Delete the employee
        // Because the Employee entity is the "owner" of the @ManyToMany team relationship
        // (it has the @JoinTable annotation), Hibernate will automatically and safely
        // delete the records in the `employee_teams` join table before deleting the employee.
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional
    public ResponseDTO assignEmployeeToTeam(Long employeeId, Long teamId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Employee not found")
                );

        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new ResourceNotFoundException("Team not found")
        );

//        Business Rule : Must be in the same department
        if(
                employee.getDepartment() == null ||
                        !employee.getDepartment().getId().equals(
                                team.getDepartment().getId()
                        )
        ){
            throw new BusinessRuleException("An employee can only join teams within their assign department. ");
        }

        employee.addTeam(team);
        return employeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public ResponseDTO removeEmployeeFromTeam(Long employeeId, Long teamId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                ()-> new ResourceNotFoundException("Employee not found")
        );

        Team team = teamRepository.findById(teamId).orElseThrow(
                ()-> new ResourceNotFoundException("Team not found")
        );

        employee.removeTeam(team);

        // NEW BUSINESS RULE: Juniors must always have at least one team
        if (employee.getRole() == EmployeeRole.JUNIOR && employee.getTeams().size() == 1) {
            throw new BusinessRuleException(
                    "Junior employees must belong to at least one team. Reassign them to a new team before removing this one."
            );
        }

        employee.removeTeam(team);
        return employeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public ResponseDTO updateEmployee(Long id, RequestDTO dto) {

            // 1. Fetch the existing employee
            Employee existingEmployee = employeeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

            // 2. Business Rule: Check if they are changing their email to one that already exists
            if (!existingEmployee.getEmail().equals(dto.getEmail()) &&
                    employeeRepository.existsByEmail(dto.getEmail())) {
                throw new BusinessRuleException("Email already in use by another employee: " + dto.getEmail());
            }

            // 3. Business Rule: Juniors must have a department
            if (dto.getRole() == EmployeeRole.JUNIOR && dto.getDepartmentId() == null) {
                throw new BusinessRuleException("Junior employees must be assigned to a department.");
            }

            // 4. Update the basic fields
            existingEmployee.setFirstName(dto.getFirstName());
            existingEmployee.setLastName(dto.getLastName());
            existingEmployee.setEmail(dto.getEmail());
            existingEmployee.setRole(dto.getRole());

            // 5. Update the Department if it was changed
            if (dto.getDepartmentId() != null) {
                // Only hit the database if the ID is actually different
                if (existingEmployee.getDepartment() == null ||
                        !existingEmployee.getDepartment().getId().equals(dto.getDepartmentId())) {

                    Department newDepartment = departmentRepository.findById(dto.getDepartmentId())
                            .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
                    existingEmployee.setDepartment(newDepartment);
                }
            } else {
                existingEmployee.setDepartment(null); // They were removed from their department
            }

            // 6. Save and return
            // (Note: We do not update teams here. Teams are handled by the assign/remove endpoints)
            return employeeMapper.toResponseDto(employeeRepository.save(existingEmployee));
    }
}
