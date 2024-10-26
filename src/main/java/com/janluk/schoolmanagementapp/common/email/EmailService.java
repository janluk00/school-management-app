package com.janluk.schoolmanagementapp.common.email;

public interface EmailService {

    void sendNotification(String receiverEmail, String passwordConfirmationToken);
}
