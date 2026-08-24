package com.employee.management.city;

import com.employee.management.city.dtos.RequestDTO;
import com.employee.management.city.dtos.ResponseDTO;
import com.employee.management.entity.City;
import com.employee.management.entity.Country;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.country.CountryRepository;
import com.employee.management.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LocationRepository locationRepository; // Injected for safe deletion
    private final CityMapper cityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDTO> getAllCities() {
        return cityRepository.findAll().stream()
                .map(cityMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + id));
        return cityMapper.toResponseDto(city);
    }

    @Override
    @Transactional
    public ResponseDTO createCity(RequestDTO dto) {
        if (dto.getCountryId() == null) {
            throw new BusinessRuleException("A city must belong to a country.");
        }

        Country country = countryRepository.findById(dto.getCountryId())
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with ID: " + dto.getCountryId()));

        City city = cityMapper.toEntity(dto);
        city.setCountry(country);

        return cityMapper.toResponseDto(cityRepository.save(city));
    }

    @Override
    @Transactional
    public ResponseDTO updateCity(Long id, RequestDTO dto) {
        City existingCity = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with ID: " + id));

        existingCity.setName(dto.getName());

        if (dto.getCountryId() != null) {
            if (existingCity.getCountry() == null ||
                    !existingCity.getCountry().getId().equals(dto.getCountryId())) {

                Country newCountry = countryRepository.findById(dto.getCountryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Country not found with ID: " + dto.getCountryId()));
                existingCity.setCountry(newCountry);
            }
        } else {
            throw new BusinessRuleException("A city must belong to a country.");
        }

        return cityMapper.toResponseDto(cityRepository.save(existingCity));
    }

    @Override
    @Transactional
    public void deleteCity(Long id) {
        if (!cityRepository.existsById(id)) {
            throw new ResourceNotFoundException("City not found with ID: " + id);
        }

        if (locationRepository.existsByCityId(id)) {
            throw new BusinessRuleException("Cannot delete this city because locations (addresses) are still tied to it.");
        }

        cityRepository.deleteById(id);
    }
}
