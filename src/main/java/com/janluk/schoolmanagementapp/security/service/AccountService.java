package com.janluk.schoolmanagementapp.security.service;

import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.repository.port.UserRepository;
import com.janluk.schoolmanagementapp.security.schema.ConfirmPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void confirmPassword(String token, ConfirmPasswordRequest request) {
        UserEntity user = userRepository.getByPasswordConfirmationToken(token);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPasswordConfirmationToken(null);
    }
}
