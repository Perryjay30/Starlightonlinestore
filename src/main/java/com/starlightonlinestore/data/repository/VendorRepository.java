package com.starlightonlinestore.data.repository;

import com.starlightonlinestore.data.models.Status;
import com.starlightonlinestore.data.models.Vendor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@Transactional
public interface VendorRepository extends JpaRepository<Vendor, Integer> {
    Optional<Vendor> findByEmail(String email);

    Optional<Vendor> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE Vendor vendor SET vendor.status = ?1 WHERE vendor.email = ?2")
    void enableVendor(Status verified, String email);
}
