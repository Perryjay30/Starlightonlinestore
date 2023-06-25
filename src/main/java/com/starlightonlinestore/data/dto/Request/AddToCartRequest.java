package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import lombok.Data;

@Data
public class AddToCartRequest {
    private Integer productId;
    private int quantity;
}
