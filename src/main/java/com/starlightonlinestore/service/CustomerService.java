package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import jakarta.mail.MessagingException;


public interface CustomerService {
    String register(CustomerRegistrationRequest customerRegistrationRequest);

    CustomerRegistrationResponse createAccount(String email, VerifyOtpRequest verifyOtpRequest);

    void verifyOTP(VerifyOtpRequest verifyOtpRequest);

    String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;

    StoreResponse resetPassword(String email, ResetPasswordRequest resetPasswordRequest);

    String sendOTP(SendOtpRequest sendOtpRequest);


    LoginResponse login(LoginRequest loginRequest);

    StoreResponse deleteCustomer(int id, DeleteRequest deleteRequest);

    StoreResponse changePassword(String email, ChangePasswordRequest changePasswordRequest);

    StoreResponse updateCustomer(Integer id, EditCustomerProfileRequest editCustomerProfileRequest);
}
