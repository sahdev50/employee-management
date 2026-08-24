package com.employee.management.department;

import com.employee.management.entity.Department;
import com.employee.management.department.dtos.RequestDTO;
import com.employee.management.department.dtos.ResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    // 1. Entity -> Response DTO (Outgoing)
    @Mapping(source = "building.id", target = "buildingId")
    ResponseDTO toResponseDto(Department department);

    // 2. Request DTO -> Entity (Incoming)
    @Mapping(source = "buildingId", target = "building.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Department toEntity(RequestDTO requestDto);
}