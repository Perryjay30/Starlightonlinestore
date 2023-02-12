package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddProductRequest {
    @NotBlank(message = "This field is required")
    private String name;
    @NotBlank(message = "This field is required")
    private BigDecimal price;
    @NotBlank(message = "This field is required")
    private ProductCategory category;
    @NotBlank(message = "This field is required")
    public int productQuantity;
}
