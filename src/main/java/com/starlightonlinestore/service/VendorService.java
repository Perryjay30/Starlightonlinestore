package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.AddProductRequest;
import com.starlightonlinestore.data.dto.Request.CreateVendorRequest;
import com.starlightonlinestore.data.dto.Request.LoginRequest;
import com.starlightonlinestore.data.dto.Request.UpdateRequest;
import com.starlightonlinestore.data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.data.dto.Response.LoginResponse;
import com.starlightonlinestore.data.dto.Response.Response;

public interface VendorService {
CreateVendorResponse createVendor(CreateVendorRequest createVendorRequest);
LoginResponse login(LoginRequest loginRequest);
Response updateVendor(UpdateRequest updateRequest);
Response deleteVendor(int id);

Response addProduct(int id, AddProductRequest addProductRequest);

}
