package com.starlightonlinestore.data.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
   @NotBlank(message = "This field is required")
   private String email;
   @NotBlank(message = "This field is required")
   private String password;
}
