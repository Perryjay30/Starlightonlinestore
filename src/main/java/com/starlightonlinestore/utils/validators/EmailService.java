package com.starlightonlinestore.utils.validators;

import jakarta.mail.MessagingException;

public interface EmailService {
    void send(String to, String email);

    void sendEmail(String recipientEmail, String name,  String link) throws MessagingException;

    String buildEmail(String name, String link);
}
