package com.starlightonlinestore.Data.Repository;

import com.starlightonlinestore.Data.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductRepository extends JpaRepository<Product, Integer> {

}
