package com.starlightonlinestore.Controller;

import com.starlightonlinestore.Data.dto.Request.AddProductRequest;
import com.starlightonlinestore.Data.dto.Request.ProductUpdateRequest;
import com.starlightonlinestore.Service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(addProductRequest));
    }

    @PatchMapping
    public ResponseEntity<?> updateProduct(@RequestBody ProductUpdateRequest productUpdateRequest) {
        return ResponseEntity.ok(productService.updateProduct(productUpdateRequest));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(int id) {
       return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
