package com.starlightonlinestore.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Data
//@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer cartId;
    private Integer productId;
    private String productName;
    private ProductCategory productCategory;
    private double unitPrice;
    private int quantity;
    private LocalDateTime localDateTime;
    private Double totalPrice;
}
