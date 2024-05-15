package com.janluk.schoolmanagementapp.security.service;

import com.janluk.schoolmanagementapp.common.exception.NoSuchUserException;
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
        return userRepository
                .getByEmail(username)
                .map(SecurityUser::new)
                .orElseThrow(() -> new NoSuchUserException("User with email %s not found!".formatted(username)));
    }
}
