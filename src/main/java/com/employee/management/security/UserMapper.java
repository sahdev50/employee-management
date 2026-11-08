package com.employee.management.security;


import com.employee.management.entity.User;
import com.employee.management.security.dtos.RequestDTO;
import com.employee.management.security.dtos.ResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface UserMapper {

    ResponseDTO toResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RequestDTO requestDto);
}
