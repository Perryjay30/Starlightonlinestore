package com.starlightonlinestore.Controller;

import com.starlightonlinestore.Data.dto.Request.CustomerRegistrationRequest;
import com.starlightonlinestore.Data.dto.Request.UpdateRequest;
import com.starlightonlinestore.Service.CustomerService;
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
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCustomerById(int id) {
      return ResponseEntity.ok(customerService.deleteCustomer(id));
    }

    @PatchMapping
    public ResponseEntity<?> updateCustomer(@RequestBody UpdateRequest updateRequest) {
       return ResponseEntity.ok(customerService.updateCustomer(updateRequest));
    }

}
