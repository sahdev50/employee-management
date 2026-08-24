package com.employee.management.location;

import com.employee.management.entity.City;
import com.employee.management.entity.Location;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.building.BuildingRepository;
import com.employee.management.city.CityRepository;
import com.employee.management.location.dtos.RequestDTO;
import com.employee.management.location.dtos.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;
    private final BuildingRepository buildingRepository; // Injected to enforce business rules
    private final LocationMapper locationMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDTO> getAllLocations() {
        return locationRepository.findAll().stream()
                .map(locationMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));
        return locationMapper.toResponseDto(location);
    }

    @Override
    @Transactional
    public ResponseDTO createLocation(RequestDTO dto) {
        // Business Rule: A location address must belong to a city
        if (dto.getCityId() == null) {
            throw new BusinessRuleException("A location must be assigned to a city upon creation.");
        }

        City city = cityRepository.findById(dto.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + dto.getCityId()));

        Location location = locationMapper.toEntity(dto);
        location.setCity(city);

        return locationMapper.toResponseDto(locationRepository.save(location));
    }

    @Override
    @Transactional
    public ResponseDTO updateLocation(Long id, RequestDTO dto) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + id));

        // Update the basic address details
        existingLocation.setStreetAddress(dto.getStreetAddress());
        existingLocation.setPostalCode(dto.getPostalCode());

        // Update the associated city if it was changed
        if (dto.getCityId() != null) {
            if (existingLocation.getCity() == null ||
                    !existingLocation.getCity().getId().equals(dto.getCityId())) {

                City newCity = cityRepository.findById(dto.getCityId())
                        .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + dto.getCityId()));
                existingLocation.setCity(newCity);
            }
        } else {
            throw new BusinessRuleException("A location cannot have its city removed.");
        }

        return locationMapper.toResponseDto(locationRepository.save(existingLocation));
    }

    @Override
    @Transactional
    public void deleteLocation(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Location not found with ID: " + id);
        }

        // Business Rule: Safe Deletion
        // Prevent deleting this address if buildings are still registered here.
        if (buildingRepository.existsByLocationId(id)) {
            throw new BusinessRuleException(
                    "Cannot delete this location because there are buildings registered at this address."
            );
        }

        locationRepository.deleteById(id);
    }
}