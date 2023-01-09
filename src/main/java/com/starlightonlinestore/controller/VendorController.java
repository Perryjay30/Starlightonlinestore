package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.CreateVendorRequest;
import com.starlightonlinestore.data.dto.Request.LoginRequest;
import com.starlightonlinestore.data.dto.Request.UpdateRequest;
import com.starlightonlinestore.service.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/vendor/")
public class VendorController {
    @Autowired
    private VendorService vendorService;

    @PostMapping
    public ResponseEntity<?> createVendor(@RequestBody CreateVendorRequest createVendorRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorService.createVendor(createVendorRequest));
    }

    @GetMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(vendorService.login(loginRequest));
    }

    @PatchMapping
    public ResponseEntity<?> updateVendor(@RequestBody UpdateRequest updateRequest) {
        return ResponseEntity.ok(vendorService.updateVendor(updateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVendor(@PathVariable int id) {
        return ResponseEntity.ok(vendorService.deleteVendor(id));
    }

}
