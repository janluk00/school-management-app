package com.janluk.schoolmanagementapp.security.service;

import com.janluk.schoolmanagementapp.common.repository.port.UserRepository;
import com.janluk.schoolmanagementapp.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomizedUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public SecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SecurityUser(userRepository.getByEmail(username));

    }
}
