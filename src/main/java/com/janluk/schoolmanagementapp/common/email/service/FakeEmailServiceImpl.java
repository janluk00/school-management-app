package com.janluk.schoolmanagementapp.common.email.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import static com.janluk.schoolmanagementapp.common.email.config.EmailProperties.*;

@Slf4j
public class FakeEmailServiceImpl implements EmailService {

    @Value("${application.url}")
    private String applicationUrl;

    @Override
    public void sendNotification(String receiverEmail, String passwordConfirmationToken) {
        log.info("Email with title: %s".formatted(CONFIRMATION_TITLE));
        log.info("With content: %s%s/%s".formatted(applicationUrl, ACCOUNT_CONFIRM_URI, passwordConfirmationToken));
        log.info("Sent from %s to %s".formatted(FAKE_SENDER, receiverEmail));
    }
}
