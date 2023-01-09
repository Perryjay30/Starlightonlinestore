package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.CustomerRegistrationRequest;
import com.starlightonlinestore.data.dto.Request.LoginRequest;
import com.starlightonlinestore.data.dto.Request.OrderProductRequest;
import com.starlightonlinestore.data.dto.Request.UpdateRequest;
import com.starlightonlinestore.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody CustomerRegistrationRequest
                                              customerRegistrationRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.register(customerRegistrationRequest));
    }


    @GetMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(customerService.login(loginRequest));
    }
//    @GetMapping
//    public ResponseEntity<?> getAllCustomers() {
//        return ResponseEntity.ok(customerService.getAllCustomers());
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable int id) {
        log.info("Id -> {}", id);
      return ResponseEntity.ok(customerService.deleteCustomer(id));
    }

    @PatchMapping
    public ResponseEntity<?> updateCustomer(@RequestBody UpdateRequest updateRequest) {
       return ResponseEntity.ok(customerService.updateCustomer(updateRequest));
    }

    @PostMapping("/order")
    public ResponseEntity<?> orderProduct(@RequestBody OrderProductRequest orderProductRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.orderProduct(orderProductRequest));
    }

}
