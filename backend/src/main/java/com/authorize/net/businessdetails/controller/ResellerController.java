package com.authorize.net.businessdetails.controller;

import com.authorize.net.businessdetails.dto.ApiResponse;
import com.authorize.net.businessdetails.dto.ResellerDto;
import com.authorize.net.businessdetails.entity.Reseller;
import com.authorize.net.businessdetails.repository.ResellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/reseller")
@RequiredArgsConstructor
public class ResellerController {

    private final ResellerRepository resellerRepository;

    /**
     * Get reseller information by business ID (reseller ID)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResellerDto>> getResellerById(@PathVariable Long id) {
        log.info("GET request for reseller ID: {}", id);
        try {
            Reseller reseller = resellerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reseller not found with ID: " + id));
            return ResponseEntity.ok(ApiResponse.ok(ResellerDto.fromEntity(reseller)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
