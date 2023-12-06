package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.data.dto.Response.LoginResponse;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import jakarta.mail.MessagingException;

public interface VendorService {
String register(CreateVendorRequest createVendorRequest);
CreateVendorResponse createAccount(String email, VerifyOtpRequest verifyOtpRequest);

void verifyOTP(VerifyOtpRequest verifyOtpRequest);

//String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;
//
//StoreResponse resetPassword(String email, ResetPasswordRequest resetPasswordRequest);

String sendOTP(SendOtpRequest sendOtpRequest);
LoginResponse login(LoginRequest loginRequest);
StoreResponse updateVendor(Integer id, UpdateVendorRequest updateVendorRequest);

StoreResponse deleteVendor(Integer id, DeleteRequest deleteRequest);
StoreResponse changePassword(String email, ChangePasswordRequest changePasswordRequest);

StoreResponse addProduct(AddProductRequest addProductRequest);

}
