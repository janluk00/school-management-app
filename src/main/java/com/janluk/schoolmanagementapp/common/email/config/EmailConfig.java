package com.janluk.schoolmanagementapp.common.email.config;

import com.janluk.schoolmanagementapp.common.email.service.EmailService;
import com.janluk.schoolmanagementapp.common.email.service.EmailServiceImpl;
import com.janluk.schoolmanagementapp.common.email.service.FakeEmailServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class EmailConfig {

    @Bean
    @ConditionalOnProperty(value = "use-email-sender", havingValue = "true")
    public EmailService emailService(JavaMailSender javaMailSender) {
        return new EmailServiceImpl(javaMailSender);
    }

    @Bean
    @ConditionalOnProperty(value = "use-email-sender", havingValue = "false", matchIfMissing = true)
    public EmailService fakeEmailService() {
        return new FakeEmailServiceImpl();
    }
}
