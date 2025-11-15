package com.authorize.net.businessdetails.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SmsService {

    /**
     * Mock SMS service for OTP delivery
     * In production, integrate with real SMS provider (Twilio, AWS SNS, etc.)
     */
    public void sendOtp(String phoneNumber, String otp) {
        log.info("Sending OTP to phone: {}", phoneNumber);
        log.info("OTP Code: {}", otp);
        
        // Mock implementation
        // In production, uncomment and configure real SMS provider
        /*
        try {
            // Example: Using Twilio
            // Message message = Twilio.getRestClient().messages.create(
            //     new PhoneNumber("+1" + phoneNumber),
            //     new PhoneNumber("+1234567890"),
            //     "Your OTP is: " + otp + ". Valid for 5 minutes."
            // );
            // log.info("SMS sent successfully: {}", message.sid);
        } catch (Exception e) {
            log.error("Failed to send SMS", e);
            throw new RuntimeException("Failed to send SMS OTP");
        }
        */
    }

    public boolean verifyPhoneFormat(String phoneNumber) {
        // Simple validation: should contain only digits and be 10-15 characters
        return phoneNumber != null && phoneNumber.replaceAll("[^0-9]", "").length() >= 10;
    }
}
