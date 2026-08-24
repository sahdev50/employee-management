package com.employee.management.department;

import com.employee.management.department.dtos.RequestDTO;
import com.employee.management.department.dtos.ResponseDTO;
import com.employee.management.entity.Building;
import com.employee.management.entity.Department;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.building.BuildingRepository;
import com.employee.management.employee.EmployeeRepository;
import com.employee.management.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{
    private final DepartmentRepository departmentRepository;
    private final BuildingRepository buildingRepository;
    private final DepartmentMapper departmentMapper;
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    @Override
    @Transactional(readOnly = true)
    public List<ResponseDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Department not found")
        );

        return departmentMapper.toResponseDto(department);
    }

    @Override
    @Transactional
    public ResponseDTO createDepartment(RequestDTO dto) {
        Department department = departmentMapper.toEntity(dto);

        if(dto.getBuildingId() != null){
            Building building = buildingRepository.findById(dto.getBuildingId()).orElseThrow(
                    ()-> new ResourceNotFoundException("Building not found")
            );

            department.setBuilding(building);
        }
        return departmentMapper.toResponseDto(department);
    }

    @Override
    @Transactional
    public ResponseDTO updateDepartment(Long id, RequestDTO dto) {
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + id));

        // Update the department name (assuming you are using the enum DepartmentName)
        existingDepartment.setName(dto.getName());

        // Update the building relationship
        if (dto.getBuildingId() != null) {
            // Only query the DB if the building actually changed
            if (existingDepartment.getBuilding() == null ||
                    !existingDepartment.getBuilding().getId().equals(dto.getBuildingId())) {

                Building newBuilding = buildingRepository.findById(dto.getBuildingId())
                        .orElseThrow(() -> new ResourceNotFoundException("Building not found with ID: " + dto.getBuildingId()));
                existingDepartment.setBuilding(newBuilding);
            }
        } else {
            existingDepartment.setBuilding(null); // The department was moved out of a physical building
        }

        return departmentMapper.toResponseDto(departmentRepository.save(existingDepartment));
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Department not found with ID: " + id);
        }

        // Business Rule: Safe Deletion check for Teams
        if (teamRepository.existsByDepartmentId(id)) {
            throw new BusinessRuleException(
                    "Cannot delete this department because active teams are still assigned to it."
            );
        }

        // Business Rule: Safe Deletion check for Employees
        if (employeeRepository.existsByDepartmentId(id)) {
            throw new BusinessRuleException(
                    "Cannot delete this department because employees are still assigned to it."
            );
        }

        departmentRepository.deleteById(id);
    }
}
