package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    private int id;
    private String name;
    private BigDecimal price;
    private ProductCategory category;
    public int productQuantity;
}
