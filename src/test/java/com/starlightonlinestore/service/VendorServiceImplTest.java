package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.models.ProductCategory;
import com.starlightonlinestore.data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.data.dto.Response.LoginResponse;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VendorServiceImplTest {

    @Autowired
    private VendorService vendorService;

    @Test
    void testThatVendorCanRegister() {
        CreateVendorRequest createVendorRequest = new CreateVendorRequest();
        createVendorRequest.setStoreName("Perry Technologies");
        createVendorRequest.setEmailAddress("mrjesus3003@gmail.com");
        createVendorRequest.setPassword("Iamnotalone#12");
        String answer = vendorService.register(createVendorRequest);
        assertEquals("Token successfully sent to your email. Please check.", answer);
    }

    @Test
    void testThatVendorAccountHasBeenCreated() {
        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        verifyOtpRequest.setToken("1058");
//        verifyOtpRequest.setEmail("mrjesus3003@gmail.com");
        CreateVendorResponse vendorResponse =
                vendorService.createAccount("", verifyOtpRequest);
        assertEquals("Successfully registered", vendorResponse.getMessage());
    }


    @Test
    void testThatVendorCanLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("helloworld@gmail.com");
        loginRequest.setPassword("Iamnotalone#12");
        LoginResponse loginResponse = vendorService.login(loginRequest);
        assertEquals("login is successful", loginResponse.getMessage());
    }

    @Test
    void testThatVendorCanChangePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
//        changePasswordRequest.setEmail("helloworld@gmail.com");
        changePasswordRequest.setOldPassword("Iamnotalone#12");
        changePasswordRequest.setNewPassword("ChangePass!28");
        StoreResponse resp = vendorService.changePassword("", changePasswordRequest);
        assertEquals("Your password has been successfully changed", resp.getMessage());
    }

    @Test
    void testThatForgotPasswordMethodWorks() throws MessagingException {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("helloworld@gmail.com");
        var response = vendorService.forgotPassword(forgotPasswordRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatPasswordCanBeResetAfterForgotten() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("3122");
//        resetPasswordRequest.setEmail("helloworld@gmail.com");
        resetPasswordRequest.setPassword("Nightingale@90");
        resetPasswordRequest.setConfirmPassword("Nightingale@90");
        StoreResponse answer = vendorService.resetPassword("", resetPasswordRequest);
        assertEquals("Your password has been reset successfully", answer.getMessage());
    }


    @Test
    void updateVendor() {
        UpdateVendorRequest requestUpdate = new UpdateVendorRequest();
        requestUpdate.setPhone("09161931557");
        requestUpdate.setEmail("daredevil@yahoo.com");
        requestUpdate.setStoreAddress("312 Portland region, Oregon");
        requestUpdate.setStoreName("Heartland Software");
        StoreResponse updateResponse = vendorService.updateVendor(1, requestUpdate);
        System.out.println(updateResponse);
        assertEquals("Vendor has been updated", updateResponse.getMessage());
    }

    @Test
    void deleteVendor() {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setPassword("Nightingale@90");
        StoreResponse delResponse = vendorService.deleteVendor(1, deleteRequest);
        System.out.println(delResponse);
        assertEquals("Vendor deleted", delResponse.getMessage());
    }
//
    @Test
    void testThatVendorCanAddProduct() {
        AddProductRequest productRequest = new AddProductRequest();
        productRequest.setName("MacBook Pro");
        productRequest.setPrice(460000.00);
        productRequest.setProductQuantity(10);
        productRequest.setCategory(ProductCategory.COMPUTING);
        StoreResponse response = vendorService.addProduct(1, productRequest);
        assertEquals("Product has been added successfully", response.getMessage());
    }
}