package com.starlightonlinestore.data.dto.Request;


import lombok.Data;

@Data
public class ProductPurchaseRequest {
    private int customerId;
    private int productId;
    private int quantity;
    private String name;
}
