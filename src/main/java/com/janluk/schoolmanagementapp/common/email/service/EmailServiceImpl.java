package com.janluk.schoolmanagementapp.common.email.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.janluk.schoolmanagementapp.common.email.config.EmailProperties.ACCOUNT_CONFIRM_URI;
import static com.janluk.schoolmanagementapp.common.email.config.EmailProperties.CONFIRMATION_TITLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService{

    @Value("${application.url}")
    private String applicationUrl;

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender javaMailSender;

    @Async
    public void sendNotification(String receiver, String passwordConfirmationToken) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(receiver);
        mail.setFrom(sender);
        mail.setSubject(CONFIRMATION_TITLE);
        mail.setText("%s%s/%s".formatted(applicationUrl, ACCOUNT_CONFIRM_URI, passwordConfirmationToken));
        javaMailSender.send(mail);

        log.info("Mail with token: %s successfully sent to: %s".formatted(passwordConfirmationToken, receiver));
    }
}
