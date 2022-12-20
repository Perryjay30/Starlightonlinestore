package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.Models.Customer;
import com.starlightonlinestore.Data.dto.Request.CustomerRegistrationRequest;
import com.starlightonlinestore.Data.dto.Request.LoginRequest;
import com.starlightonlinestore.Data.dto.Request.ProductPurchaseRequest;
import com.starlightonlinestore.Data.dto.Request.UpdateRequest;
import com.starlightonlinestore.Data.dto.Response.CustomerRegistrationResponse;
import com.starlightonlinestore.Data.dto.Response.LoginResponse;
import com.starlightonlinestore.Data.dto.Response.ProductPurchaseResponse;
import com.starlightonlinestore.Data.dto.Response.Response;

import java.util.List;


public interface CustomerService {
    CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest);
    LoginResponse login(LoginRequest loginRequest);
    Response deleteCustomer(int id);
    List<Customer> getAllCustomers();

    ProductPurchaseResponse orderProduct
            (ProductPurchaseRequest productPurchaseRequest);
    Response updateCustomer(UpdateRequest updateRequest);
}
