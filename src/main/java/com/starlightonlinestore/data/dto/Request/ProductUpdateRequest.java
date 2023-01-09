package com.starlightonlinestore.data.dto.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    public int id;
    public String name;
    public BigDecimal price;
    public String category;
    public int quantity;
}
