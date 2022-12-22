package com.starlightonlinestore.Data.dto.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private int id;
    private String name;
    private BigDecimal price;
    private String category;
    public int productQuantity;
}
