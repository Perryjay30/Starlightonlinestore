package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddProductRequest {
    @NotBlank(message = "This field is required")
    private String productName;
    @NotNull(message = "This field is required")
    private Double price;
    @NotNull(message = "This field is required")
    private String category;
    @NotNull(message = "This field is required")
    private int productQuantity;
}
