package com.employee.management.team;

import com.employee.management.entity.Department;
import com.employee.management.entity.Team;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.department.DepartmentRepository;
import com.employee.management.employee.EmployeeRepository;
import com.employee.management.team.dtos.RequestDTO;
import com.employee.management.team.dtos.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository; // Injected to enforce business rules
    private final TeamMapper teamMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + id));
        return teamMapper.toResponseDto(team);
    }

    @Override
    @Transactional
    public ResponseDTO createTeam(RequestDTO dto) {
        // Business Rule: A team cannot exist without a department
        if (dto.getDepartmentId() == null) {
            throw new BusinessRuleException("A team must be assigned to a department upon creation.");
        }

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));

        Team team = teamMapper.toEntity(dto);
        team.setDepartment(department);

        return teamMapper.toResponseDto(teamRepository.save(team));
    }

    @Override
    @Transactional
    public ResponseDTO updateTeam(Long id, RequestDTO dto) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + id));

        // Update basic fields
        existingTeam.setName(dto.getName());

        // Update the department if it was changed
        if (dto.getDepartmentId() != null) {
            if (existingTeam.getDepartment() == null ||
                    !existingTeam.getDepartment().getId().equals(dto.getDepartmentId())) {

                Department newDepartment = departmentRepository.findById(dto.getDepartmentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + dto.getDepartmentId()));
                existingTeam.setDepartment(newDepartment);
            }
        } else {
            throw new BusinessRuleException("A team cannot have its department removed. It must belong to a department.");
        }

        return teamMapper.toResponseDto(teamRepository.save(existingTeam));
    }

    @Override
    @Transactional
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Team not found with ID: " + id);
        }

        // Business Rule: Do not delete teams that have active employees
        // (This uses the custom @Query method we added to EmployeeRepository earlier)
        if (employeeRepository.hasActiveEmployees(id)) {
            throw new BusinessRuleException(
                    "Cannot delete this team. Please reassign or remove all employees from the team first."
            );
        }

        teamRepository.deleteById(id);
    }
}
