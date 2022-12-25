package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.Models.Customer;
import com.starlightonlinestore.Data.dto.Request.*;
import com.starlightonlinestore.Data.dto.Response.*;

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
