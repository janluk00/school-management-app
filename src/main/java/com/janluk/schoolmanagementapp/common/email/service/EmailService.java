package com.janluk.schoolmanagementapp.common.email.service;

public interface EmailService {

    void sendNotification(String receiverEmail, String passwordConfirmationToken);
}
