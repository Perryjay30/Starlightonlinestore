package com.starlightonlinestore.Data.Repository;

import com.starlightonlinestore.Data.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    Optional<Customer> findByEmail(String email);
}
