package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static com.starlightonlinestore.data.models.Role.USER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Test
    void testThatUserCanRegister() {
//        CustomerRegistrationRequest firstCustomerRegisterRequest = new CustomerRegistrationRequest();
//        firstCustomerRegisterRequest.setFirstName("Steve");
//        firstCustomerRegisterRequest.setLastName("Deborah");
//        firstCustomerRegisterRequest.setEmail("chidioke56@email.com");
//        firstCustomerRegisterRequest.setPassword("Tomisin97#");
        CustomerRegistrationRequest secondCustomerRegisterRequest = new CustomerRegistrationRequest();
        secondCustomerRegisterRequest.setFirstName("DrinkWater");
        secondCustomerRegisterRequest.setLastName("Danny");
        secondCustomerRegisterRequest.setEmail("o.taiwo@native.semicolon.africa");
        secondCustomerRegisterRequest.setPassword("Dasilva19@");
        String response =
                userService.register(secondCustomerRegisterRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatUserAccountHasBeenCreated() {
        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        verifyOtpRequest.setToken("2601");
//        verifyOtpRequest.setEmail("adebolexsewa@gmail.com");
        CustomerRegistrationResponse registrationResponse =
                userService.createAccount("o.taiwo@native.semicolon.africa", verifyOtpRequest);
        assertEquals("User registration successful", registrationResponse.getMessage());
    }

    @Test
    void testThatUserCanLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("mrjesus@email.com");
        loginRequest.setPassword("Dasilva19@");
        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("successful login", loginResponse.getMessage());
    }

    @Test
    void testThatUserCanChangePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
//        changePasswordRequest.setEmail("chidioke56@email.com");
        changePasswordRequest.setOldPassword("Tomisin97#");
        changePasswordRequest.setNewPassword("ChangePass!28");
        StoreResponse resp = userService.changePassword("", changePasswordRequest);
        assertEquals("Your password has been successfully changed", resp.getMessage());
    }

    @Test
    void testThatForgotPasswordMethodWorks() throws MessagingException {
        ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("mrjesus@email.com");
        var response = userService.forgotPassword(forgotPasswordRequest);
        assertEquals("Token successfully sent to your email. Please check.", response);
    }

    @Test
    void testThatPasswordCanBeResetAfterForgotten() {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("5971");
//        resetPasswordRequest.setEmail("mrjesus@email.com");
        resetPasswordRequest.setPassword("Washington@11");
        resetPasswordRequest.setConfirmPassword("Washington@11");
        StoreResponse answer = userService.resetPassword("o.taiwo@native.semicolon.africa", resetPasswordRequest);
        assertEquals("Your password has been reset successfully", answer.getMessage());
    }


    @Test
    void testThatUserCanBeUpdated() {
        EditCustomerProfileRequest editCustomerProfileRequest = new EditCustomerProfileRequest();
        editCustomerProfileRequest.setEmail("Emailisupdated@gmail.com");
        editCustomerProfileRequest.setFirstName("Hakimi");
        editCustomerProfileRequest.setLastName("Davies");
        editCustomerProfileRequest.setPhone( "07035893966");
//        updateCustomer.setPassword("Englandmoro678#");
        StoreResponse updateCustomerResponse =
                userService.updateCustomer(302, editCustomerProfileRequest);
        System.out.println(updateCustomerResponse);
        assertEquals("Customer updated successfully", updateCustomerResponse.getMessage());
    }

    @Test
    void testThatUserCanBeDeleted() {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setPassword("Egunperry@57");
        StoreResponse deleteResponse = userService.deleteCustomer(252, deleteRequest);
        assertEquals("Customer deleted", deleteResponse.getMessage());
    }

    @Test
    void testThatSuperAdminCAnAssignRole() throws MessagingException {
        AssignRoleRequest assignRoleRequest = new AssignRoleRequest();
        assignRoleRequest.setEmail("o.taiwo@native.semicolon.africa");
        assignRoleRequest.setUserRole(String.valueOf(USER));
        StoreResponse response = userService.assignRoles(assignRoleRequest);
        assertNotNull(response);
    }

}