package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private String productName;
    private Double unitPrice;
    private ProductCategory category;
    private int productQuantity;
}
