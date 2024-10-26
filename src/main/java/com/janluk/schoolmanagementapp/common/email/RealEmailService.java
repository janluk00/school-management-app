package com.janluk.schoolmanagementapp.common.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;

import static com.janluk.schoolmanagementapp.common.email.EmailProperties.ACCOUNT_CONFIRM_URI;
import static com.janluk.schoolmanagementapp.common.email.EmailProperties.CONFIRMATION_TITLE;

@Slf4j
class RealEmailService implements EmailService {

    private final String applicationUrl;
    private final String sender;
    private final JavaMailSender javaMailSender;

    public RealEmailService(
            @Value("${application.url}") String applicationUrl,
            @Value("${spring.mail.username}") String sender,
            JavaMailSender javaMailSender
    ) {
        this.applicationUrl = applicationUrl;
        this.sender = sender;
        this.javaMailSender = javaMailSender;
    }


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
