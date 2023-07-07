package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.service.VendorService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/vendor/")
@CrossOrigin(origins = "*")
public class VendorController {
    @Autowired
    private VendorService vendorService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateVendorRequest createVendorRequest) {
        return ResponseEntity.ok(vendorService.register(createVendorRequest));
    }

    @PostMapping("/createAccount/{email}")
    public ResponseEntity<?> createVendor(@PathVariable String email, @Valid @RequestBody VerifyOtpRequest verifyOtpRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.createAccount(email, verifyOtpRequest));
    }

    @GetMapping("/vendorLogin")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(vendorService.login(loginRequest));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        return ResponseEntity.ok(vendorService.forgotPassword(forgotPasswordRequest));
    }

    @PostMapping("/resetPassword/{email}")
    public ResponseEntity<?> resetPassword(@PathVariable String email, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return ResponseEntity.ok(vendorService.resetPassword(email, resetPasswordRequest));
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<?> changePassword(@PathVariable String email, @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(vendorService.changePassword(email, changePasswordRequest));
    }

    @DeleteMapping("/deleteVendor/{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable int id, @Valid @RequestBody DeleteRequest deleteRequest) {
//        log.info("Id -> {}", id);
        return ResponseEntity.ok(vendorService.deleteVendor(id, deleteRequest));
    }

    @PatchMapping("/updateVendorProfile/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable int id, @Valid @RequestBody UpdateVendorRequest updateVendorRequest) {
        return ResponseEntity.ok(vendorService.updateVendor(id, updateVendorRequest));
    }

    @PostMapping("/addProduct/{id}")
    public ResponseEntity<?> addProduct(@PathVariable int id, @Valid @RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok(vendorService.addProduct(id, addProductRequest));
    }

}
