package com.starlightonlinestore.service;

import com.starlightonlinestore.data.models.ProductCategory;
import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import com.starlightonlinestore.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerServiceImplTest {
    @Autowired
    private CustomerService customerService;
    private CustomerRegistrationRequest firstBuyerRegisterRequest;
    private CustomerRegistrationRequest secondBuyerRegisterRequest;
    private OrderProductRequest orderProductRequest;
    @BeforeEach
    void setUp() {
        firstBuyerRegisterRequest = new CustomerRegistrationRequest();
        firstBuyerRegisterRequest.setEmail("tomisin21@gmail.com");
        firstBuyerRegisterRequest.setPassword("Tomisin97#");
        firstBuyerRegisterRequest.setPhoneNumber("09075432169");
        firstBuyerRegisterRequest.setAddress("145 Jolaosho str, Enugu");

        secondBuyerRegisterRequest = new CustomerRegistrationRequest();
        secondBuyerRegisterRequest.setEmail("chidioke56@email.com");
        secondBuyerRegisterRequest.setAddress("89, tejuola street, mushin, Lagos");
        secondBuyerRegisterRequest.setPhoneNumber("08134576954");
        secondBuyerRegisterRequest.setPassword("Dasilva19@");

//        productPurchaseRequest = new ProductPurchaseRequest();
//        productPurchaseRequest.setProductId(1);
//        productPurchaseRequest.setQuantity(2);
//        productPurchaseRequest.setCustomerId();


//        addProductRequest = new AddProductRequest();
//        addProductRequest.setCategory("Appliances");
//        addProductRequest.setName("Gas cooker");
//        addProductRequest.setPrice(650.00);

    }

    @Test
    void testThatCustomerCanRegister() {
        CustomerRegistrationResponse response =
                customerService.register(firstBuyerRegisterRequest);
        CustomerRegistrationResponse response1 =
                customerService.register(secondBuyerRegisterRequest);
        System.out.println(response);
        System.out.println(response1);
        assertEquals(response.getStatusCode(), 201);
        assertEquals("User registration successful", response1.getMessage());
    }

    @Test
    void testThatCustomerCanLogin() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(firstBuyerRegisterRequest.getEmail());
        loginRequest.setPassword(firstBuyerRegisterRequest.getPassword());
        LoginResponse loginResponse = customerService.login(loginRequest);
        System.out.println(loginResponse);
        assertEquals("successful login", loginResponse.getMessage());
    }


    @Test
    void testThatCustomerCanBeUpdated() {
        UpdateRequest updateCustomer = new UpdateRequest();
        updateCustomer.setId(152);
        updateCustomer.setEmail("Emailisupdated@gmail.com");
        updateCustomer.setFirstName("Hakimi");
        updateCustomer.setLastName("Davies");
        updateCustomer.setPhone( "07035893966");
        updateCustomer.setPassword("Englandmoro678#");
        Response updateCustomerResponse =
                customerService.updateCustomer(updateCustomer);
        System.out.println(updateCustomerResponse);
        assertEquals("Customer updated successfully", updateCustomerResponse.getMessage());
    }

    @Test
    void testThatCustomerCanBeDeleted() {
        Response deleteResponse = customerService.deleteCustomer(1);
        System.out.println(deleteResponse);
        assertEquals("Customer deleted", deleteResponse.getMessage());
    }

    @Test
    void testThatCustomerCanOrderProduct() {
        orderProductRequest = new OrderProductRequest();
        orderProductRequest.setCustomerId(1);
        orderProductRequest.setQuantity(4);
        orderProductRequest.setProductName("Versace, Turtle-neck");
        orderProductRequest.setProductCategory(ProductCategory.GROCERIES);
        orderProductRequest.setPrice(17000.00);
        OrderProductResponse orderProductResponse = customerService.orderProduct(orderProductRequest);
        System.out.println(orderProductResponse);
        assertEquals(201, orderProductResponse.getStatusCode());
    }






}