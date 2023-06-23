package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.AddToCartRequest;
import com.starlightonlinestore.data.dto.Request.OrderProductRequest;
import com.starlightonlinestore.data.dto.Request.PaymentRequest;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import com.starlightonlinestore.data.models.CustomerOrder;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.util.List;

public interface CartingAndOrderProductService {
    StoreResponse addProductToCart
            (Integer id, AddToCartRequest addToCartRequest);
    StoreResponse removeProductFromCart(Integer customerId, Integer productInCartId);
    List<CustomerOrder> getAllOrders();
    StoreResponse orderProduct(Integer id, OrderProductRequest orderProductRequest);
    StoreResponse CustomerCanMakePaymentForGoodsOrdered
            (Integer customerId, Integer orderId, PaymentRequest paymentRequest) throws IOException, MessagingException;
}
