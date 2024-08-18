package com.janluk.schoolmanagementapp.common.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
class EmailConfig {

    @Bean
    @ConditionalOnProperty(value = "use-email-sender", havingValue = "real")
    public EmailService realEmailService(
            @Value("${application.url}") String applicationUrl,
            @Value("${spring.mail.username}") String sender,
            JavaMailSender javaMailSender
    ) {
        return new RealEmailService(applicationUrl, sender, javaMailSender);
    }

    @Bean
    @ConditionalOnProperty(value = "use-email-sender", havingValue = "fake", matchIfMissing = true)
    public EmailService fakeEmailService(@Value("${application.url}") String applicationUrl) {
        return new FakeEmailService(applicationUrl);
    }
}
