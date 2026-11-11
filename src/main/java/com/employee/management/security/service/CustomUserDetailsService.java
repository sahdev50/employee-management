package com.employee.management.security.service;

import com.employee.management.entity.User;
import com.employee.management.entity.UserPrinciple;
import com.employee.management.security.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if(user == null){
            System.out.println("User not found : 404");
            throw new UsernameNotFoundException("User not found! : 404");
        }

        return new UserPrinciple(user);
    }
}
