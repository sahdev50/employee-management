package com.employee.management.employee;


import com.employee.management.employee.dtos.RequestDTO;
import com.employee.management.employee.dtos.ResponseDTO;

import com.employee.management.entity.Employee;
import com.employee.management.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    // 1. Entity -> Response DTO (Outgoing)
    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(target = "teamIds", expression = "java(mapTeamsToIds(employee.getTeams()))")
    ResponseDTO toResponseDto(Employee employee);

    // 2. Request DTO -> Entity (Incoming)
    @Mapping(source = "departmentId", target = "department.id")
    @Mapping(target = "teams", ignore = true) // Handled separately
    @Mapping(target = "id", ignore = true) // DB generates this
    @Mapping(target = "createdAt", ignore = true) // DB generates this
    @Mapping(target = "updatedAt", ignore = true) // DB generates this
    Employee toEntity(RequestDTO requestDto);

    // Helper method
    default Set<Long> mapTeamsToIds(Set<Team> teams) {
        if (teams == null) return null;
        return teams.stream()
                .map(Team::getId)
                .collect(Collectors.toSet());
    }
}
