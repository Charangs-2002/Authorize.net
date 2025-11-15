package com.authorize.net.businessdetails.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpRequest {
    private Long businessId;
    private String recipient;
    private String method;
    private String fieldName;
}
