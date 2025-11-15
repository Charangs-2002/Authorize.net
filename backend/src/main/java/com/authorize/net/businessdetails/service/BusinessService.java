package com.authorize.net.businessdetails.service;

import com.authorize.net.businessdetails.dto.BusinessDto;
import com.authorize.net.businessdetails.entity.Business;
import com.authorize.net.businessdetails.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BusinessService {

    private final BusinessRepository businessRepository;

    public BusinessDto getBusinessById(Long id) {
        log.info("Fetching business with ID: {}", id);
        Business business = businessRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Business not found with ID: " + id));
        return BusinessDto.fromEntity(business);
    }

    public BusinessDto updateBusiness(Long id, BusinessDto businessDto) {
        log.info("Updating business with ID: {}", id);
        
        Business business = businessRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Business not found with ID: " + id));

        // Check for email uniqueness if changed
        if (!business.getEmail().equals(businessDto.getEmail())) {
            businessRepository.findByEmail(businessDto.getEmail())
                .ifPresent(b -> {
                    if (!b.getId().equals(id)) {
                        throw new IllegalArgumentException("Email already exists");
                    }
                });
        }

        // Check for phone uniqueness if changed
        if (!business.getPhone().equals(businessDto.getPhone())) {
            businessRepository.findByPhone(businessDto.getPhone())
                .ifPresent(b -> {
                    if (!b.getId().equals(id)) {
                        throw new IllegalArgumentException("Phone already exists");
                    }
                });
        }

        // Update fields
        business.setName(businessDto.getName());
        business.setEmail(businessDto.getEmail());
        business.setPhone(businessDto.getPhone());
        business.setWebsite(businessDto.getWebsite());
        business.setAddress(businessDto.getAddress());
        business.setCity(businessDto.getCity());
        business.setState(businessDto.getState());
        business.setZipCode(businessDto.getZipCode());

        Business updatedBusiness = businessRepository.save(business);
        log.info("Business updated successfully with ID: {}", id);
        return BusinessDto.fromEntity(updatedBusiness);
    }

    public List<BusinessDto> getAllBusinesses() {
        log.info("Fetching all businesses");
        return businessRepository.findAll()
            .stream()
            .map(BusinessDto::fromEntity)
            .collect(Collectors.toList());
    }

    public BusinessDto createBusiness(BusinessDto businessDto) {
        log.info("Creating new business: {}", businessDto.getName());
        
        // Check email uniqueness
        if (businessRepository.findByEmail(businessDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Check phone uniqueness
        if (businessRepository.findByPhone(businessDto.getPhone()).isPresent()) {
            throw new IllegalArgumentException("Phone already exists");
        }

        Business business = businessDto.toEntity();
        Business savedBusiness = businessRepository.save(business);
        log.info("Business created successfully with ID: {}", savedBusiness.getId());
        return BusinessDto.fromEntity(savedBusiness);
    }

    public void deleteBusiness(Long id) {
        log.info("Deleting business with ID: {}", id);
        if (!businessRepository.existsById(id)) {
            throw new IllegalArgumentException("Business not found with ID: " + id);
        }
        businessRepository.deleteById(id);
        log.info("Business deleted successfully with ID: {}", id);
    }
}
