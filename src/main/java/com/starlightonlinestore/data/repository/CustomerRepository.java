package com.starlightonlinestore.data.repository;

import com.starlightonlinestore.data.models.Customer;
import com.starlightonlinestore.data.models.Status;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("UPDATE Customer customer SET customer.status = ?1 WHERE customer.email = ?2")
    void enableCustomer(Status verified, String email);
}
