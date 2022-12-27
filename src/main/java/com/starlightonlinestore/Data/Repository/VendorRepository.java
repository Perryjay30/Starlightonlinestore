package com.starlightonlinestore.Data.Repository;

import com.starlightonlinestore.Data.Models.Customer;
import com.starlightonlinestore.Data.Models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Optional<Vendor> findByEmail(String email);

    Optional<Vendor> findByPhoneNumber(String phoneNumber);
}
