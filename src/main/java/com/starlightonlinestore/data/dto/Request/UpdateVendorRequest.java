package com.starlightonlinestore.data.dto.Request;

import lombok.Data;

@Data
public class UpdateVendorRequest {
    private String storeName;
    private String email;
    private String phone;
    private String storeAddress;
}
