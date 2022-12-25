package com.starlightonlinestore.Data.Models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String productName;
    private ProductCategory productCategory;
    private double price;
    private int quantity;
    private double total;
}
