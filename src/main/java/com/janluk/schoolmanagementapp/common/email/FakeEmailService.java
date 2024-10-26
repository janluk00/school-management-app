package com.janluk.schoolmanagementapp.common.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import static com.janluk.schoolmanagementapp.common.email.EmailProperties.*;

@Slf4j
class FakeEmailService implements EmailService {

    private final String applicationUrl;

    public FakeEmailService(@Value("${application.url}") String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    @Override
    public void sendNotification(String receiverEmail, String passwordConfirmationToken) {
        log.info("Email with title: %s".formatted(CONFIRMATION_TITLE));
        log.info("With content: %s%s/%s".formatted(applicationUrl, ACCOUNT_CONFIRM_URI, passwordConfirmationToken));
        log.info("Sent from %s to %s".formatted(FAKE_SENDER, receiverEmail));
    }
}
