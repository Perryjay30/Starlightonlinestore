package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.Models.Product;
import com.starlightonlinestore.Data.dto.Request.AddProductRequest;
import com.starlightonlinestore.Data.dto.Request.CreateVendorRequest;
import com.starlightonlinestore.Data.dto.Request.LoginRequest;
import com.starlightonlinestore.Data.dto.Request.UpdateRequest;
import com.starlightonlinestore.Data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.Data.dto.Response.LoginResponse;
import com.starlightonlinestore.Data.dto.Response.Response;

public interface VendorService {
CreateVendorResponse createVendor(CreateVendorRequest createVendorRequest);
LoginResponse login(LoginRequest loginRequest);
Response updateVendor(UpdateRequest updateRequest);
Response deleteVendor(int id);

Response addProduct(int id, AddProductRequest addProductRequest);

}
