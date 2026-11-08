package com.employee.management.security.controller;



import com.employee.management.security.dtos.RequestDTO;
import com.employee.management.security.dtos.ResponseDTO;
import com.employee.management.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/userssss")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> createUser(
            @Valid @RequestBody RequestDTO requestDTO) {

        ResponseDTO created = userService.signUp(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
