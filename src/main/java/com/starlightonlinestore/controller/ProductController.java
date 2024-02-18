package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.AddProductRequest;
import com.starlightonlinestore.data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.data.models.Product;
import com.starlightonlinestore.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;
    @PostMapping
    @PreAuthorize("hasAuthority('VENDOR, SUPER_ADMIN, ADMIN')")
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(addProductRequest));
    }

    @GetMapping("/viewAllProducts/{userId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> viewAllProducts(@PathVariable int userId) {
       return ResponseEntity.ok(productService.viewAllProduct(userId));
    }

    @PatchMapping("/editProduct/{productId}")
    @PreAuthorize("hasAuthority('VENDOR')")
    public ResponseEntity<?> updateProduct(@PathVariable Integer productId, @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        return ResponseEntity.ok(productService.updateProduct(productId, productUpdateRequest));
    }

    @DeleteMapping("/{id}/{productId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer id, @PathVariable int productId) {
//        log.info("Id -> {}", id);
       return ResponseEntity.ok(productService.deleteProduct(id, productId));
    }
}
