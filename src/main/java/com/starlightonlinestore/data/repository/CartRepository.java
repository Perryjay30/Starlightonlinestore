package com.starlightonlinestore.data.repository;

import com.starlightonlinestore.data.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
