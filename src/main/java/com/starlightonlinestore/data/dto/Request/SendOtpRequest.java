package com.starlightonlinestore.data.dto.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class  SendOtpRequest {
    @NotBlank(message = "This field must not be empty")
    private @Email String email;
}
