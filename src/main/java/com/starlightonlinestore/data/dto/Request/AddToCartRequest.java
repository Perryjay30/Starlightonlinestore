package com.starlightonlinestore.data.dto.Request;

import com.starlightonlinestore.data.models.ProductCategory;
import lombok.Data;

@Data
public class AddToCartRequest {
    private int productId;
    private int quantity;
}
