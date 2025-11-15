package com.authorize.net.businessdetails.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp", indexes = {
    @Index(name = "idx_business_id", columnList = "business_id"),
    @Index(name = "idx_recipient", columnList = "recipient")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long businessId;

    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OtpStatus status;

    @Column(nullable = false)
    private String fieldName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "attempts")
    private Integer attempts = 0;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean isVerified() {
        return status == OtpStatus.VERIFIED;
    }

    public enum DeliveryMethod {
        EMAIL,
        SMS
    }

    public enum OtpStatus {
        PENDING,
        VERIFIED,
        EXPIRED,
        MAX_ATTEMPTS_EXCEEDED
    }
}
