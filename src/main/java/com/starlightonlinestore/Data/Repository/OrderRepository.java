package com.starlightonlinestore.Data.Repository;

import com.starlightonlinestore.Data.Models.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {

}
