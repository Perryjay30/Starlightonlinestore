package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import com.starlightonlinestore.data.models.CustomerOrder;
import jakarta.mail.MessagingException;

import java.util.List;


public interface CustomerService {
    String register(CustomerRegistrationRequest customerRegistrationRequest);

    CustomerRegistrationResponse createAccount(VerifyOtpRequest verifyOtpRequest);

    void verifyOTP(VerifyOtpRequest verifyOtpRequest);

    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;

    Response resetPassword(ResetPasswordRequest resetPasswordRequest);

    String sendOTP(SendOtpRequest sendOtpRequest);
    LoginResponse login(LoginRequest loginRequest);
    Response deleteCustomer(int id, DeleteRequest deleteRequest);
    List<CustomerOrder> getAllOrders();

    Response changePassword(ChangePasswordRequest changePasswordRequest);

    Response addProductToCart
            (Integer id, AddToCartRequest addToCartRequest);
    Response orderProduct(Integer id, OrderProductRequest orderProductRequest);
    Response updateCustomer(Integer id, UpdateRequest updateRequest);
}
