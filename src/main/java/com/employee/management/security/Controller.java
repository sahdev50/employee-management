//package com.employee.management.security;
//
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.web.csrf.CsrfToken;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/v1/security")
//@RequiredArgsConstructor
//public class Controller {
//
//    @GetMapping("/csrf-token")
//    public CsrfToken getCsrfToken(HttpServletRequest request){
//        return (CsrfToken) request.getAttribute("_csrf");
//    }
//
//    @GetMapping("/hello")
//    public ResponseEntity<String> helloworld(){
//        return ResponseEntity.ok("Hello, World!");
//    }
//
//}
