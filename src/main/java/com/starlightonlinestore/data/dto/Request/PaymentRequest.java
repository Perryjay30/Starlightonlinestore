package com.starlightonlinestore.data.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String name;
    private String description;
    private double amount;
}
