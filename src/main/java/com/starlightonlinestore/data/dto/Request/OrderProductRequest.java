package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderProductRequest {
    private String deliveryAddress;
}
