package com.starlightonlinestore.data.dto.Response;

import lombok.Data;

@Data
public class CustomerRegistrationResponse {
    private int userId;
    private String message;
    private int statusCode;
}
