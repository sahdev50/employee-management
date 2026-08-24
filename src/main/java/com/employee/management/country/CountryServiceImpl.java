package com.employee.management.country;

import java.util.List;

import com.employee.management.country.dtos.RequestDTO;
import com.employee.management.country.dtos.ResponseDTO;
import com.employee.management.entity.Country;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.exception.ResourceNotFoundException;
import com.employee.management.city.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository; // Injected for safe deletion
    private final CountryMapper countryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDTO> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(countryMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with ID: " + id));
        return countryMapper.toResponseDto(country);
    }


    @Override
    @Transactional
    public ResponseDTO createCountry(RequestDTO dto) {
        // Note: If they pass an isoCode or name that already exists,
        // our GlobalExceptionHandler will catch the Database Constraint Violation automatically!
        Country country = countryMapper.toEntity(dto);
        return countryMapper.toResponseDto(countryRepository.save(country));
    }

    @Override
    @Transactional
    public ResponseDTO updateCountry(Long id, RequestDTO dto) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country not found with ID: " + id));

        existingCountry.setName(dto.getName());
        existingCountry.setIsoCode(dto.getIsoCode());

        return countryMapper.toResponseDto(countryRepository.save(existingCountry));
    }

    @Override
    @Transactional
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Country not found with ID: " + id);
        }

        if (cityRepository.existsByCountryId(id)) {
            throw new BusinessRuleException("Cannot delete this country because cities are still tied to it.");
        }

        countryRepository.deleteById(id);
    }
}