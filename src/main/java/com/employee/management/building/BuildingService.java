package com.employee.management.building;

import com.employee.management.building.dtos.RequestDTO;
import com.employee.management.building.dtos.ResponseDTO;

import java.util.List;

public interface BuildingService {
    List<ResponseDTO> getAllBuildings();
    ResponseDTO getBuildingById(Long id);
    ResponseDTO createBuilding(RequestDTO dto);
    ResponseDTO updateBuilding(Long id, RequestDTO dto); // For editing
    void deleteBuilding(Long id);
}
