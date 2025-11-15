package com.authorize.net.businessdetails.controller;

import com.authorize.net.businessdetails.dto.ApiResponse;
import com.authorize.net.businessdetails.dto.BusinessDto;
import com.authorize.net.businessdetails.service.BusinessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

    private final BusinessService businessService;

    /**
     * Get business details by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessDto>> getBusinessById(@PathVariable Long id) {
        log.info("GET request for business ID: {}", id);
        try {
            BusinessDto business = businessService.getBusinessById(id);
            return ResponseEntity.ok(ApiResponse.ok(business));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Get all businesses
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BusinessDto>>> getAllBusinesses() {
        log.info("GET request for all businesses");
        List<BusinessDto> businesses = businessService.getAllBusinesses();
        return ResponseEntity.ok(ApiResponse.ok(businesses));
    }

    /**
     * Create new business
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BusinessDto>> createBusiness(@RequestBody BusinessDto businessDto) {
        log.info("POST request to create business: {}", businessDto.getName());
        try {
            BusinessDto createdBusiness = businessService.createBusiness(businessDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Business created successfully", createdBusiness));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Update business details
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BusinessDto>> updateBusiness(
            @PathVariable Long id,
            @RequestBody BusinessDto businessDto) {
        log.info("PUT request to update business ID: {}", id);
        try {
            BusinessDto updatedBusiness = businessService.updateBusiness(id, businessDto);
            return ResponseEntity.ok(ApiResponse.ok("Business updated successfully", updatedBusiness));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Delete business
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBusiness(@PathVariable Long id) {
        log.info("DELETE request for business ID: {}", id);
        try {
            businessService.deleteBusiness(id);
            return ResponseEntity.ok(ApiResponse.ok("Business deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}
