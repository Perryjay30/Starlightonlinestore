package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderProductRequest {
    @NotBlank(message = "This field is required")
    private String productName;
    @NotBlank(message = "This field is required")
    private ProductCategory productCategory;
    @NotBlank(message = "This field is required")
    private double price;
    @NotBlank(message = "This field is required")
    private int quantity;
}
