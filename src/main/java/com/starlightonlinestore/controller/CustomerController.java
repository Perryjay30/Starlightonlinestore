package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.service.CustomerService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/customer")
@Slf4j
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerRegistrationRequest
                                              customerRegistrationRequest) {
        return ResponseEntity.ok(customerService.register(customerRegistrationRequest));
    }

    @PostMapping("/createAccount/{email}")
    public ResponseEntity<?> createAccount(@PathVariable String email, @Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createAccount(email, verifyOtpRequest));
    }


    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(customerService.login(loginRequest));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        return ResponseEntity.ok(customerService.forgotPassword(forgotPasswordRequest));
    }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(customerService.resetPassword(email, resetPasswordRequest));
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(customerService.changePassword(changePasswordRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomerById(@Valid @RequestBody @PathVariable int id, DeleteRequest deleteRequest) {
        log.info("Id -> {}", id);
      return ResponseEntity.ok(customerService.deleteCustomer(id, deleteRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody @PathVariable int id, UpdateRequest updateRequest) {
       return ResponseEntity.ok(customerService.updateCustomer(id, updateRequest));
    }

}
