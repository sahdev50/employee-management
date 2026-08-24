package com.employee.management.city;

import com.employee.management.city.dtos.RequestDTO;
import com.employee.management.city.dtos.ResponseDTO;
import com.employee.management.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(source = "country.id", target = "countryId")
    ResponseDTO toResponseDto(City city);

    @Mapping(source = "countryId", target = "country.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    City toEntity(RequestDTO requestDto);
}
