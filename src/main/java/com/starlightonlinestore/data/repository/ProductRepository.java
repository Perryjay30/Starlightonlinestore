package com.starlightonlinestore.data.repository;

import com.starlightonlinestore.data.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductRepository extends JpaRepository<Product, Integer> {

}
