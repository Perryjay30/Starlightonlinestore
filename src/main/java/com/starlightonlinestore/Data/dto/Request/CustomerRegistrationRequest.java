package com.starlightonlinestore.Data.dto.Request;

import lombok.Data;

@Data
public class CustomerRegistrationRequest {
    private String email;
    private String password;
    private String phoneNumber;
    private String address;


}
