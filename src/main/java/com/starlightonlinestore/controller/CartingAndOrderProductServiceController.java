package com.starlightonlinestore.controller;

import com.starlightonlinestore.data.dto.Request.AddToCartRequest;
import com.starlightonlinestore.data.dto.Request.OrderProductRequest;
import com.starlightonlinestore.data.dto.Request.PaymentRequest;
import com.starlightonlinestore.service.CartingAndOrderProductService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cartingAndProduct")
public class CartingAndOrderProductServiceController {

    private final CartingAndOrderProductService cartingAndOrderProductService;

    public CartingAndOrderProductServiceController(CartingAndOrderProductService cartingAndOrderProductService) {
        this.cartingAndOrderProductService = cartingAndOrderProductService;
    }

    @PostMapping("/carting/{customerId}")
    public ResponseEntity<?> addProductToCart(@PathVariable int customerId, @Valid @RequestBody AddToCartRequest addToCartRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartingAndOrderProductService.addProductToCart(customerId, addToCartRequest));
    }

    @GetMapping("/allOrders/{customerId}")
    public ResponseEntity<?> getAllMyOrders(@PathVariable Integer customerId) {
        return ResponseEntity.ok(cartingAndOrderProductService.getAllOrders(customerId));
    }

    @PostMapping("/placeOrder/{customerId}")
    public ResponseEntity<?> orderProducts(@PathVariable int customerId, @RequestBody OrderProductRequest orderProductRequest) {
        return ResponseEntity.ok(cartingAndOrderProductService.orderProduct(customerId, orderProductRequest));
    }

    @DeleteMapping("/deleteProductFromCart/{customerId}/{productInCartId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Integer customerId, @PathVariable Integer productInCartId) {
        return ResponseEntity.ok(cartingAndOrderProductService.removeProductFromCart(customerId, productInCartId));
    }

    @PostMapping("/makePayment/{customerId}/{orderId}")
    public ResponseEntity<?> makePaymentForGoods(@PathVariable Integer customerId, @PathVariable Integer orderId, @RequestBody PaymentRequest paymentRequest) throws IOException, MessagingException {
        return ResponseEntity.ok(cartingAndOrderProductService.CustomerCanMakePaymentForGoodsOrdered(customerId, orderId, paymentRequest));
    }
}
