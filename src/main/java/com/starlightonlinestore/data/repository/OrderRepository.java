package com.starlightonlinestore.data.repository;

import com.starlightonlinestore.data.models.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {

}
