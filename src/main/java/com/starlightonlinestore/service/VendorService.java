package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.data.dto.Response.LoginResponse;
import com.starlightonlinestore.data.dto.Response.Response;
import jakarta.mail.MessagingException;

public interface VendorService {
String register(CreateVendorRequest createVendorRequest);
CreateVendorResponse createAccount(VerifyOtpRequest verifyOtpRequest);

void verifyOTP(VerifyOtpRequest verifyOtpRequest);

String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException;

Response resetPassword(ResetPasswordRequest resetPasswordRequest);

String sendOTP(SendOtpRequest sendOtpRequest);
LoginResponse login(LoginRequest loginRequest);
Response updateVendor(Integer id, UpdateRequest updateRequest);

Response deleteVendor(Integer id, DeleteRequest deleteRequest);
Response changePassword(ChangePasswordRequest changePasswordRequest);

Response addProduct(int id, AddProductRequest addProductRequest);

}
