package com.starlightonlinestore.data.dto.Response;

import lombok.Data;

@Data
public class OrderProductResponse {
    private int id;
    private int statusCode;
    private String message;
}
