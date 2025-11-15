package com.authorize.net.businessdetails.controller;

import com.authorize.net.businessdetails.dto.ApiResponse;
import com.authorize.net.businessdetails.dto.OtpRequest;
import com.authorize.net.businessdetails.dto.OtpResponse;
import com.authorize.net.businessdetails.dto.OtpVerifyRequest;
import com.authorize.net.businessdetails.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    /**
     * Send OTP to email or SMS
     */
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<OtpResponse>> sendOtp(@RequestBody OtpRequest request) {
        log.info("POST request to send OTP for business: {}", request.getBusinessId());
        try {
            OtpResponse response = otpService.sendOtp(request);
            if (response.getSuccess()) {
                return ResponseEntity.ok(ApiResponse.ok("OTP sent successfully", response));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(response.getMessage(), response));
            }
        } catch (Exception e) {
            log.error("Error sending OTP", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to send OTP: " + e.getMessage()));
        }
    }

    /**
     * Verify OTP
     */
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<OtpResponse>> verifyOtp(@RequestBody OtpVerifyRequest request) {
        log.info("POST request to verify OTP for business: {}", request.getBusinessId());
        try {
            OtpResponse response = otpService.verifyOtp(request);
            if (response.getSuccess()) {
                return ResponseEntity.ok(ApiResponse.ok("OTP verified successfully", response));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(response.getMessage(), response));
            }
        } catch (Exception e) {
            log.error("Error verifying OTP", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Failed to verify OTP: " + e.getMessage()));
        }
    }
}
