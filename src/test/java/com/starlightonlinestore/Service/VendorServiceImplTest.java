package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.dto.Request.CreateVendorRequest;
import com.starlightonlinestore.Data.dto.Request.LoginRequest;
import com.starlightonlinestore.Data.dto.Request.UpdateRequest;
import com.starlightonlinestore.Data.dto.Response.CreateVendorResponse;
import com.starlightonlinestore.Data.dto.Response.LoginResponse;
import com.starlightonlinestore.Data.dto.Response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VendorServiceImplTest {

    @Autowired
    private VendorService vendorService;

    private CreateVendorRequest createVendorRequest;

    @BeforeEach
    void setUp() {
        createVendorRequest = new CreateVendorRequest();
        createVendorRequest.setStoreName("Ikeja City Mall");
        createVendorRequest.setPhoneNumber("08123459204");
        createVendorRequest.setEmailAddress("helloworld@gmail.com");
        createVendorRequest.setPassword("Iamnotalone#12");
        createVendorRequest.setStoreAddress("No 45, Abiola way, Lagos");
    }

    @Test
    void createVendor() {
        CreateVendorResponse vendorResponse =
                vendorService.createVendor(createVendorRequest);
        System.out.println(vendorResponse);
        assertEquals("Successfully registered", vendorResponse.getMessage());
    }

    @Test
    void login() {
        LoginRequest login = new LoginRequest();
        login.setEmail(createVendorRequest.getEmailAddress());
        login.setPassword(createVendorRequest.getPassword());
        LoginResponse response = vendorService.login(login);
        System.out.println(response);
        assertEquals("login is successful", response.getMessage());
    }

    @Test
    void updateVendor() {
        UpdateRequest requestUpdate = new UpdateRequest();
        requestUpdate.setId(2);
        requestUpdate.setPhone("09161931557");
        requestUpdate.setPassword("Youwillnever!17");
        requestUpdate.setEmail("daredevil@yahoo.com");

        Response updateResponse = vendorService.updateVendor(requestUpdate);
        System.out.println(updateResponse);
        assertEquals("Vendor has been updated", updateResponse.getMessage());
    }

    @Test
    void deleteVendor() {
        Response delResponse = vendorService.deleteVendor(102);
        System.out.println(delResponse);
        assertEquals("Deleted", delResponse.getMessage());
    }
}