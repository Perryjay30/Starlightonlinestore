package com.starlightonlinestore.data.dto.Request;

import lombok.Data;

@Data
public class UpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String storeName;
    private String deliveryAddress;
    private String storeAddress;
}
