package com.authorize.net.businessdetails.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Send OTP via email
     */
    public void sendOtp(String toEmail, String otp) {
        log.info("Sending OTP email to: {}", toEmail);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP for Business Details Update - Authorize.Net");
            message.setText(buildOtpEmailBody(otp));
            message.setFrom("noreply@authorize.net");

            mailSender.send(message);
            log.info("OTP email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send OTP email", e);
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }

    /**
     * Build OTP email body
     */
    private String buildOtpEmailBody(String otp) {
        return "Hello,\n\n" +
            "Your One-Time Password (OTP) for updating your business details is:\n\n" +
            otp + "\n\n" +
            "This OTP is valid for 5 minutes only.\n" +
            "Do not share this code with anyone.\n\n" +
            "If you did not request this code, please ignore this email.\n\n" +
            "Best regards,\n" +
            "Authorize.Net Team";
    }

    /**
     * Send notification email
     */
    public void sendNotificationEmail(String toEmail, String subject, String body) {
        log.info("Sending notification email to: {}", toEmail);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@authorize.net");

            mailSender.send(message);
            log.info("Notification email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send notification email", e);
        }
    }
}
