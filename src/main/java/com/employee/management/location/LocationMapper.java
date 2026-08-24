package com.employee.management.location;

import com.employee.management.entity.Location;
import com.employee.management.location.dtos.RequestDTO;
import com.employee.management.location.dtos.ResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    @Mapping(source = "city.id", target = "cityId")
    ResponseDTO toResponseDto(Location location);

    @Mapping(source = "cityId", target = "city.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Location toEntity(RequestDTO requestDto);
}
