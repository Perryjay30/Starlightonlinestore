package com.starlightonlinestore.service;

import com.starlightonlinestore.data.models.ProductCategory;
import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerServiceImplTest {
    @Autowired
    private CustomerService customerService;

    @Test
    void testThatCustomerCanRegister() {
//        CustomerRegistrationRequest firstCustomerRegisterRequest = new CustomerRegistrationRequest();
//        firstCustomerRegisterRequest.setFirstName("Steve");
//        firstCustomerRegisterRequest.setLastName("Deborah");
//        firstCustomerRegisterRequest.setEmail("chidioke56@email.com");
//        firstCustomerRegisterRequest.setPassword("Tomisin97#");
        CustomerRegistrationRequest secondCustomerRegisterRequest = new CustomerRegistrationRequest();
        secondCustomerRegisterRequest.setFirstName("Ajoke");
        secondCustomerRegisterRequest.setLastName("Taiwo");
        secondCustomerRegisterRequest.setEmail("adebolexsewa@gmail.com");
        secondCustomerRegisterRequest.setPassword("Dasilva19@");
        String response =
                customerService.register(secondCustomerRegisterRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatCustomerAccountHasBeenCreated() {
        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        verifyOtpRequest.setToken("0661");
        verifyOtpRequest.setEmail("adebolexsewa@gmail.com");
        CustomerRegistrationResponse registrationResponse =
                customerService.createAccount(verifyOtpRequest);
        assertEquals("User registration successful", registrationResponse.getMessage());
    }

    @Test
    void testThatCustomerCanLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("mrjesus@email.com");
        loginRequest.setPassword("Dasilva19@");
        LoginResponse loginResponse = customerService.login(loginRequest);
        assertEquals("successful login", loginResponse.getMessage());
    }

    @Test
    void testThatCustomerCanChangePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setEmail("chidioke56@email.com");
        changePasswordRequest.setOldPassword("Tomisin97#");
        changePasswordRequest.setNewPassword("ChangePass!28");
        StoreResponse resp = customerService.changePassword(changePasswordRequest);
        assertEquals("Your password has been successfully changed", resp.getMessage());
    }

    @Test
    void testThatForgotPasswordMethodWorks() throws MessagingException {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("mrjesus@email.com");
        var response = customerService.forgotPassword(forgotPasswordRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatPasswordCanBeResetAfterForgotten() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("6561");
        resetPasswordRequest.setEmail("mrjesus@email.com");
        resetPasswordRequest.setPassword("Nightingale@90");
        resetPasswordRequest.setConfirmPassword("Nightingale@90");
        StoreResponse answer = customerService.resetPassword(resetPasswordRequest);
        assertEquals("Your password has been reset successfully", answer.getMessage());
    }


    @Test
    void testThatCustomerCanBeUpdated() {
        UpdateRequest updateCustomer = new UpdateRequest();
        updateCustomer.setEmail("Emailisupdated@gmail.com");
        updateCustomer.setFirstName("Hakimi");
        updateCustomer.setLastName("Davies");
        updateCustomer.setPhone( "07035893966");
        updateCustomer.setPassword("Englandmoro678#");
        StoreResponse updateCustomerResponse =
                customerService.updateCustomer(1, updateCustomer);
        System.out.println(updateCustomerResponse);
        assertEquals("Customer updated successfully", updateCustomerResponse.getMessage());
    }

    @Test
    void testThatCustomerCanBeDeleted() {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setPassword("Nightingale@90");
        StoreResponse deleteResponse = customerService.deleteCustomer(2, deleteRequest);
        assertEquals("Customer deleted", deleteResponse.getMessage());
    }

    @Test
    void testThatCustomerCanAddProductToCart() {
        AddToCartRequest addToCartRequest= new AddToCartRequest();
        addToCartRequest.setQuantity(10);
        addToCartRequest.setProductName("Dell Laptop");
        addToCartRequest.setProductCategory(ProductCategory.COMPUTING);
        addToCartRequest.setPrice(166000.00);
        StoreResponse productToCartResponse = customerService.addProductToCart(1, addToCartRequest);
        assertEquals("Product successfully added to cart", productToCartResponse.getMessage());
    }

    @Test
    void testThatCustomerCanOrderProduct() {
        OrderProductRequest orderProductRequest = new OrderProductRequest();
        orderProductRequest.setDeliveryAddress("312, Herbert Macualay, Yaba");
        StoreResponse response = customerService.orderProduct(1, orderProductRequest);
        assertEquals("Order placed", response.getMessage());
    }
}