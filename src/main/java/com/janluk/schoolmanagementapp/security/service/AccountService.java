package com.janluk.schoolmanagementapp.security.service;

import com.janluk.schoolmanagementapp.common.email.service.EmailService;
import com.janluk.schoolmanagementapp.common.model.UserEntity;
import com.janluk.schoolmanagementapp.common.repository.port.UserRepository;
import com.janluk.schoolmanagementapp.security.schema.ConfirmPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.janluk.schoolmanagementapp.common.user.TokenGenerator.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public void confirmPassword(String token, ConfirmPasswordRequest request) {
        UserEntity user = userRepository.getByPasswordConfirmationToken(token);

        user.setPassword(passwordEncoder.encode(request.password()));
        user.setPasswordConfirmationToken(null);
    }

    @Transactional
    public void changePassword(String email) {
        UserEntity user = userRepository.getByEmail(email);

        user.setPasswordConfirmationToken(generateToken());
        emailService.sendNotification(email, user.getPasswordConfirmationToken());

        userRepository.save(user);
    }
}
