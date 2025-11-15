package com.authorize.net.businessdetails.repository;

import com.authorize.net.businessdetails.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByBusinessIdAndRecipientAndStatus(
        Long businessId,
        String recipient,
        Otp.OtpStatus status
    );

    Optional<Otp> findByBusinessIdAndOtpAndStatus(
        Long businessId,
        String otp,
        Otp.OtpStatus status
    );
}
