package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import lombok.Data;

@Data
public class OrderProductRequest {
    private int customerId;
    private String productName;
    private ProductCategory productCategory;
    private double price;
    private int quantity;
}
