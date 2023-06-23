package com.starlightonlinestore.data.dto.Response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RecipientData {
    private String name;
    private String description;
    private BigDecimal amount;
    private String integration;
    private String domain;
    private String slug;
    private String currency;
    private String type;
    private Boolean collect_phone;
    private Boolean active;
    private Boolean published;
    private Boolean migrate;
    private Long id;
    private String createdAt;
    private String updatedAt;
}
