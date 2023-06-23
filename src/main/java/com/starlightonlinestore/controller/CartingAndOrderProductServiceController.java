package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.AddToCartRequest;
import com.starlightonlinestore.data.dto.Request.PaymentRequest;
import com.starlightonlinestore.service.CartingAndOrderProductService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cartingAndProductOrder")
@RequiredArgsConstructor
public class CartingAndOrderProductServiceController {

    private final CartingAndOrderProductService cartingAndOrderProductService;

    @PostMapping("/order/{id}")
    public ResponseEntity<?> addProductToCart(@Valid @RequestBody @PathVariable int id, AddToCartRequest addToCartRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartingAndOrderProductService.addProductToCart(id, addToCartRequest));
    }

    @GetMapping
    public ResponseEntity<?> getAllMyOrders() {
        return ResponseEntity.ok(cartingAndOrderProductService.getAllOrders());
    }

    @DeleteMapping("/deleteProductFromCart{customerId}/{productInCartId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Integer customerId, @PathVariable Integer productInCartId) {
        return ResponseEntity.ok(cartingAndOrderProductService.removeProductFromCart(customerId, productInCartId));
    }

    @PostMapping("/makePayment/{customerId}/{orderId}")
    public ResponseEntity<?> makePaymentForGoods(@PathVariable Integer customerId, @PathVariable Integer orderId, @RequestBody PaymentRequest paymentRequest) throws IOException, MessagingException {
        return ResponseEntity.ok(cartingAndOrderProductService.CustomerCanMakePaymentForGoodsOrdered(customerId, orderId, paymentRequest));
    }
}
