package com.authorize.net.businessdetails.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerifyRequest {
    private Long businessId;
    private String otp;
    private String method;
    private String fieldName;
}
