package com.starlightonlinestore.service;

import com.starlightonlinestore.data.models.Customer;
import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;

import java.util.List;


public interface CustomerService {
    CustomerRegistrationResponse register(CustomerRegistrationRequest customerRegistrationRequest);
    LoginResponse login(LoginRequest loginRequest);
    Response deleteCustomer(int id);
    List<Customer> getAllCustomers();

    OrderProductResponse orderProduct
            (OrderProductRequest orderProductRequest);
    Response updateCustomer(UpdateRequest updateRequest);
}
