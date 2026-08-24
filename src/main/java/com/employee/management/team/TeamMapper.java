package com.employee.management.team;

import com.employee.management.entity.Team;
import com.employee.management.team.dtos.RequestDTO;
import com.employee.management.team.dtos.ResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface TeamMapper {

    // 1. Entity -> Response DTO (Outgoing)
    @Mapping(source = "department.id", target = "departmentId")
    ResponseDTO toResponseDto(Team team);

    // 2. Request DTO -> Entity (Incoming)
    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "employees", ignore = true) // Employees are managed from the Employee side
    Team toEntity(RequestDTO requestDto);
}
