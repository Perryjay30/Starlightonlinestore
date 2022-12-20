package com.starlightonlinestore.Data.dto.Request;

import lombok.Data;

@Data
public class AddProductRequest {
    private String name;
    private double price;
    private String category;
    public int productQuantity;
}
