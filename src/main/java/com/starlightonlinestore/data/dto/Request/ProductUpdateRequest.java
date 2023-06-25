package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    @NotBlank(message = "This field is required")
    public String name;
    @NotBlank(message = "This field is required")
    public Double unitPrice;
    @NotBlank(message = "This field is required")
    public ProductCategory category;
    @NotBlank(message = "This field is required")
    public int quantity;
}
