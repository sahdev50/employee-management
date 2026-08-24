package com.employee.management.country;

import com.employee.management.country.dtos.RequestDTO;
import com.employee.management.country.dtos.ResponseDTO;

import java.util.List;

public interface CountryService {
    List<ResponseDTO> getAllCountries();
    ResponseDTO getCountryById(Long id);
    ResponseDTO createCountry(RequestDTO dto);
    ResponseDTO updateCountry(Long id, RequestDTO dto);
    void deleteCountry(Long id);
}
