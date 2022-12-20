package com.starlightonlinestore.Data.dto.Request;

import lombok.Data;

@Data
public class UpdateRequest {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String storeName;
    private String storeAddress;
}
