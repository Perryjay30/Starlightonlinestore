package com.starlightonlinestore.data.repository;

import com.starlightonlinestore.data.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Optional<Vendor> findByEmail(String email);

    Optional<Vendor> findByPhoneNumber(String phoneNumber);
}
