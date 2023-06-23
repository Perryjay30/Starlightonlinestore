package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.data.dto.Response.LoginResponse;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import jakarta.mail.MessagingException;

public interface VendorService {
String register(CreateVendorRequest createVendorRequest);
CreateVendorResponse createAccount(VerifyOtpRequest verifyOtpRequest);

void verifyOTP(VerifyOtpRequest verifyOtpRequest);

String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;

StoreResponse resetPassword(ResetPasswordRequest resetPasswordRequest);

String sendOTP(SendOtpRequest sendOtpRequest);
LoginResponse login(LoginRequest loginRequest);
StoreResponse updateVendor(Integer id, UpdateRequest updateRequest);

StoreResponse deleteVendor(Integer id, DeleteRequest deleteRequest);
StoreResponse changePassword(ChangePasswordRequest changePasswordRequest);

StoreResponse addProduct(int id, AddProductRequest addProductRequest);

}
