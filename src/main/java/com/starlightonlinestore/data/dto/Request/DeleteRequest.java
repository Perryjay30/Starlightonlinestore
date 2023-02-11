package com.starlightonlinestore.data.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteRequest {
    @NotBlank(message = "This field must not be empty")
    private String password;
}
