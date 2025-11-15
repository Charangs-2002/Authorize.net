package com.authorize.net.businessdetails.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpResponse {
    private Boolean success;
    private String message;
    private Boolean verified;
    private String otpId;
}
