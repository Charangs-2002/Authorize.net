package com.authorize.net.businessdetails.dto;

import com.authorize.net.businessdetails.entity.Reseller;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResellerDto {
    private Long id;
    private String name;
    private Double commissionRate;
    private String status;
    private String accountManager;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ResellerDto fromEntity(Reseller reseller) {
        if (reseller == null) {
            return null;
        }
        return ResellerDto.builder()
            .id(reseller.getId())
            .name(reseller.getName())
            .commissionRate(reseller.getCommissionRate())
            .status(reseller.getStatus().name())
            .accountManager(reseller.getAccountManager())
            .createdAt(reseller.getCreatedAt())
            .updatedAt(reseller.getUpdatedAt())
            .build();
    }
}
