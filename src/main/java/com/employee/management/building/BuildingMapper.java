package com.employee.management.building;

import com.employee.management.building.dtos.RequestDTO;
import com.employee.management.building.dtos.ResponseDTO;
import com.employee.management.entity.Building;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuildingMapper {

    @Mapping(source = "location.id", target = "locationId")
    ResponseDTO toResponseDto(Building building);

    @Mapping(source = "locationId", target = "location.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Building toEntity(RequestDTO requestDto);
}
