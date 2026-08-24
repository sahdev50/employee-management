package com.employee.management.country;

import com.employee.management.country.dtos.RequestDTO;
import com.employee.management.country.dtos.ResponseDTO;
import com.employee.management.entity.Country;
import org.mapstruct.Mapper;

import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    ResponseDTO toResponseDto(Country country);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Country toEntity(RequestDTO requestDto);
}
