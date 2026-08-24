package com.employee.management.building;

import com.employee.management.building.dtos.RequestDTO;
import com.employee.management.building.dtos.ResponseDTO;
import com.employee.management.entity.Building;
import com.employee.management.entity.Location;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.department.DepartmentRepository;
import com.employee.management.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;
    private final LocationRepository locationRepository;
    private final DepartmentRepository departmentRepository; // Injected for safe-deletion rules
    private final BuildingMapper buildingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(buildingMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getBuildingById(Long id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found with ID: " + id));
        return buildingMapper.toResponseDto(building);
    }

    @Override
    @Transactional
    public ResponseDTO createBuilding(RequestDTO dto) {
        // Business Rule: A building must physically exist at a registered Location
        if (dto.getLocationId() == null) {
            throw new BusinessRuleException("A building must be assigned to a location (address) upon creation.");
        }

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + dto.getLocationId()));

        Building building = buildingMapper.toEntity(dto);
        building.setLocation(location);

        return buildingMapper.toResponseDto(buildingRepository.save(building));
    }

    @Override
    @Transactional
    public ResponseDTO updateBuilding(Long id, RequestDTO dto) {
        Building existingBuilding = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found with ID: " + id));

        // Update the building name
        existingBuilding.setName(dto.getName());

        // Update the location if it was changed
        if (dto.getLocationId() != null) {
            if (existingBuilding.getLocation() == null ||
                    !existingBuilding.getLocation().getId().equals(dto.getLocationId())) {

                Location newLocation = locationRepository.findById(dto.getLocationId())
                        .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + dto.getLocationId()));
                existingBuilding.setLocation(newLocation);
            }
        } else {
            throw new BusinessRuleException("A building's location cannot be removed. It must be tied to a physical address.");
        }

        return buildingMapper.toResponseDto(buildingRepository.save(existingBuilding));
    }

    @Override
    @Transactional
    public void deleteBuilding(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Building not found with ID: " + id);
        }

        // Business Rule: Safe Deletion
        // Prevents deleting a building if departments are currently assigned to it.
        if (departmentRepository.existsByBuildingId(id)) {
            throw new BusinessRuleException(
                    "Cannot delete this building because active departments are still assigned to it. Move the departments first."
            );
        }

        buildingRepository.deleteById(id);
    }
}
