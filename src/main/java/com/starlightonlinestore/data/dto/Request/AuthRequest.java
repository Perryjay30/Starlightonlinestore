package com.starlightonlinestore.data.dto.Request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
