package com.employee.management.security.service;

import com.employee.management.entity.User;
import com.employee.management.exception.BusinessRuleException;
import com.employee.management.security.UserMapper;
import com.employee.management.security.UserRepository;
import com.employee.management.security.dtos.RequestDTO;
import com.employee.management.security.dtos.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseDTO signUp(RequestDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessRuleException("User must have unique Username");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException("User with same email already exists");
        }

        User createdUser = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .hashedPassword(bCryptPasswordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();

        return userMapper.toResponseDto(userRepository.save(createdUser));
    }
}
