package com.employee.management.city;

import com.employee.management.city.dtos.RequestDTO;
import com.employee.management.city.dtos.ResponseDTO;

import java.util.List;

public interface CityService {
    List<ResponseDTO> getAllCities();
    ResponseDTO getCityById(Long id);
    ResponseDTO createCity(RequestDTO dto);
    ResponseDTO updateCity(Long id, RequestDTO dto);
    void deleteCity(Long id);
}
