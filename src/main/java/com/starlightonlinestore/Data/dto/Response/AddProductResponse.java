package com.starlightonlinestore.Data.dto.Response;

import lombok.Data;

@Data
public class AddProductResponse {
    private String message;
    private int productId;
    private int statusCode;
}
