package com.authorize.net.businessdetails.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reseller")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reseller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double commissionRate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResellerStatus status;

    @Column(nullable = false)
    private String accountManager;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ResellerStatus {
        ACTIVE,
        INACTIVE,
        SUSPENDED
    }
}
