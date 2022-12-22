package com.starlightonlinestore.Data.Models;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @Enumerated
    private ProductCategory category;
    private int vendorId;
    private BigDecimal price;
    private int quantity;

}
