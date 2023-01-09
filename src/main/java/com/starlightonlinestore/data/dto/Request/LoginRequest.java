package com.starlightonlinestore.data.dto.Request;

import lombok.Data;

@Data
public class LoginRequest {
   private String email;
   private String password;
}
