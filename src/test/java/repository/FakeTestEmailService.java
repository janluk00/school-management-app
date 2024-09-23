package repository;

import com.janluk.schoolmanagementapp.common.email.EmailService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeTestEmailService implements EmailService {

    @Override
    public void sendNotification(String receiverEmail, String passwordConfirmationToken) {
        log.debug("Notification was sent to: %s".formatted(receiverEmail));
    }
}
