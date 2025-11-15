package com.authorize.net.businessdetails.dto;

import com.authorize.net.businessdetails.entity.Business;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDto {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String website;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Long resellerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BusinessDto fromEntity(Business business) {
        if (business == null) {
            return null;
        }
        return BusinessDto.builder()
            .id(business.getId())
            .name(business.getName())
            .email(business.getEmail())
            .phone(business.getPhone())
            .website(business.getWebsite())
            .address(business.getAddress())
            .city(business.getCity())
            .state(business.getState())
            .zipCode(business.getZipCode())
            .resellerId(business.getResellerId())
            .createdAt(business.getCreatedAt())
            .updatedAt(business.getUpdatedAt())
            .build();
    }

    public Business toEntity() {
        return Business.builder()
            .id(this.id)
            .name(this.name)
            .email(this.email)
            .phone(this.phone)
            .website(this.website)
            .address(this.address)
            .city(this.city)
            .state(this.state)
            .zipCode(this.zipCode)
            .resellerId(this.resellerId)
            .build();
    }
}
