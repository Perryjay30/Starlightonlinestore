package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import com.starlightonlinestore.data.models.User;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;


public interface UserService {
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
    StoreResponse assignRoles(AssignRoleRequest assignRoleRequest) throws MessagingException;
    User getFoundCustomer(Optional<User> customerRepository, String message);
    List<User> getAllUsers();
}
