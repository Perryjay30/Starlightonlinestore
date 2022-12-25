package com.starlightonlinestore.Data.dto.Request;

import com.starlightonlinestore.Data.Models.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductRequest {
    private int customerId;
    private String productName;
    private ProductCategory productCategory;
    private double price;
    private int quantity;
}
