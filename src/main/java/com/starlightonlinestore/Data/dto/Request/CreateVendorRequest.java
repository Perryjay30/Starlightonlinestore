package com.starlightonlinestore.Data.dto.Request;

import lombok.Data;

@Data
public class CreateVendorRequest {
    private String emailAddress;
    private String phoneNumber;
    private String password;
    private String storeName;
    private String storeAddress;

}
