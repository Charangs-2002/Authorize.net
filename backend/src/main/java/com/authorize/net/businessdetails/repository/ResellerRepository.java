package com.authorize.net.businessdetails.repository;

import com.authorize.net.businessdetails.entity.Reseller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResellerRepository extends JpaRepository<Reseller, Long> {
}
