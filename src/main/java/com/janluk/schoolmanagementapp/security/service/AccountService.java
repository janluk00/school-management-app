package com.janluk.schoolmanagementapp.security.service;

import com.janluk.schoolmanagementapp.common.email.EmailService;
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
    private final EmailService emailService;

    @Transactional
    public void confirmPassword(String token, ConfirmPasswordRequest request) {
        UserEntity user = userRepository.getByPasswordConfirmationToken(token);

        user.updatePasswordAndClearToken(passwordEncoder.encode(request.password()));
    }

    @Transactional
    public void changePassword(String email) {
        UserEntity user = userRepository.getByEmail(email);

        user.generateNewToken();
        emailService.sendNotification(email, user.getPasswordConfirmationToken());

        userRepository.save(user);
    }
}
