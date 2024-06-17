package com.janluk.schoolmanagementapp.common.user;

import com.janluk.schoolmanagementapp.common.repository.port.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }
}
