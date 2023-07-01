package com.starlightonlinestore.data.dto.Request;

import lombok.Data;

@Data
public class EditCustomerProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String deliveryAddress;
}
