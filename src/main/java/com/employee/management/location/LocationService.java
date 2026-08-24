package com.employee.management.location;

import com.employee.management.location.dtos.RequestDTO;
import com.employee.management.location.dtos.ResponseDTO;

import java.util.List;

public interface LocationService {
    List<ResponseDTO> getAllLocations();
    ResponseDTO getLocationById(Long id);
    ResponseDTO createLocation(RequestDTO dto);
    ResponseDTO updateLocation(Long id, RequestDTO dto);
    void deleteLocation(Long id);
}
