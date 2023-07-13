package com.starlightonlinestore.data.models;

import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;

@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String productName;
    private ProductCategory category;
//    @ManyToOne
//    @JoinColumn(name="vendor_id", referencedColumnName="id")
    private int vendorId;
    private Double unitPrice;
    private int quantity;

}
