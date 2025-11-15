package com.authorize.net.businessdetails.service;

import com.authorize.net.businessdetails.dto.OtpRequest;
import com.authorize.net.businessdetails.dto.OtpResponse;
import com.authorize.net.businessdetails.dto.OtpVerifyRequest;
import com.authorize.net.businessdetails.entity.Otp;
import com.authorize.net.businessdetails.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final SmsService smsService;

    @Value("${otp.expiry.minutes:5}")
    private Integer otpExpiryMinutes;

    @Value("${otp.length:6}")
    private Integer otpLength;

    @Value("${otp.max-attempts:3}")
    private Integer maxAttempts;

    /**
     * Generate and send OTP
     */
    public OtpResponse sendOtp(OtpRequest request) {
        log.info("Sending OTP to business: {} via {}", request.getBusinessId(), request.getMethod());

        // Generate OTP
        String otp = generateOtp();

        // Create OTP record
        Otp otpEntity = Otp.builder()
            .businessId(request.getBusinessId())
            .otp(otp)
            .recipient(request.getRecipient())
            .deliveryMethod(Otp.DeliveryMethod.valueOf(request.getMethod().toUpperCase()))
            .status(Otp.OtpStatus.PENDING)
            .fieldName(request.getFieldName())
            .expiresAt(LocalDateTime.now().plusMinutes(otpExpiryMinutes))
            .attempts(0)
            .build();

        Otp savedOtp = otpRepository.save(otpEntity);

        // Send OTP via appropriate channel
        try {
            if ("EMAIL".equalsIgnoreCase(request.getMethod())) {
                emailService.sendOtp(request.getRecipient(), otp);
            } else if ("SMS".equalsIgnoreCase(request.getMethod())) {
                smsService.sendOtp(request.getRecipient(), otp);
            } else {
                throw new IllegalArgumentException("Invalid delivery method: " + request.getMethod());
            }

            log.info("OTP sent successfully to: {}", request.getRecipient());
            return OtpResponse.builder()
                .success(true)
                .message("OTP sent successfully")
                .verified(false)
                .otpId(savedOtp.getId().toString())
                .build();

        } catch (Exception e) {
            log.error("Failed to send OTP", e);
            otpEntity.setStatus(Otp.OtpStatus.EXPIRED);
            otpRepository.save(otpEntity);
            
            return OtpResponse.builder()
                .success(false)
                .message("Failed to send OTP: " + e.getMessage())
                .verified(false)
                .build();
        }
    }

    /**
     * Verify OTP
     */
    public OtpResponse verifyOtp(OtpVerifyRequest request) {
        log.info("Verifying OTP for business: {}", request.getBusinessId());

        Otp otpEntity = otpRepository.findByBusinessIdAndOtpAndStatus(
                request.getBusinessId(),
                request.getOtp(),
                Otp.OtpStatus.PENDING
            ).orElse(null);

        if (otpEntity == null) {
            log.warn("OTP not found or already used for business: {}", request.getBusinessId());
            return OtpResponse.builder()
                .success(false)
                .message("Invalid OTP")
                .verified(false)
                .build();
        }

        // Check if OTP is expired
        if (otpEntity.isExpired()) {
            log.warn("OTP expired for business: {}", request.getBusinessId());
            otpEntity.setStatus(Otp.OtpStatus.EXPIRED);
            otpRepository.save(otpEntity);
            
            return OtpResponse.builder()
                .success(false)
                .message("OTP has expired")
                .verified(false)
                .build();
        }

        // Check max attempts
        otpEntity.setAttempts(otpEntity.getAttempts() + 1);
        if (otpEntity.getAttempts() > maxAttempts) {
            log.warn("Max OTP attempts exceeded for business: {}", request.getBusinessId());
            otpEntity.setStatus(Otp.OtpStatus.MAX_ATTEMPTS_EXCEEDED);
            otpRepository.save(otpEntity);
            
            return OtpResponse.builder()
                .success(false)
                .message("Maximum OTP attempts exceeded")
                .verified(false)
                .build();
        }

        // Mark as verified
        otpEntity.setStatus(Otp.OtpStatus.VERIFIED);
        otpEntity.setVerifiedAt(LocalDateTime.now());
        Otp verifiedOtp = otpRepository.save(otpEntity);

        log.info("OTP verified successfully for business: {}", request.getBusinessId());
        return OtpResponse.builder()
            .success(true)
            .message("OTP verified successfully")
            .verified(true)
            .otpId(verifiedOtp.getId().toString())
            .build();
    }

    /**
     * Generate random 6-digit OTP
     */
    private String generateOtp() {
        Random random = new Random();
        int otp = random.nextInt((int) Math.pow(10, otpLength));
        return String.format("%0" + otpLength + "d", otp);
    }

    /**
     * Check if OTP is verified for a business
     */
    public boolean isOtpVerified(Long businessId, String otp) {
        return otpRepository.findByBusinessIdAndOtpAndStatus(
                businessId,
                otp,
                Otp.OtpStatus.VERIFIED
            ).isPresent();
    }
}
