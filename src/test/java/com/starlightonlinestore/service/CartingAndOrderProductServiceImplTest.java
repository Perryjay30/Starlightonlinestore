package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.AddToCartRequest;
import com.starlightonlinestore.data.dto.Request.OrderProductRequest;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import com.starlightonlinestore.data.models.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartingAndOrderProductServiceImplTest {

    @Autowired
    private CartingAndOrderProductService cartingAndOrderProductService;

    @Test
    void testThatCustomerCanAddProductToCart() {
        AddToCartRequest addToCartRequest= new AddToCartRequest();
        addToCartRequest.setProductId(1);
        addToCartRequest.setQuantity(5);
        StoreResponse productToCartResponse = cartingAndOrderProductService.addProductToCart(1, addToCartRequest);
        assertEquals("Product successfully added to cart", productToCartResponse.getMessage());
    }

    @Test
    void testThatCustomerCanRemoveProductFromCart() {
        StoreResponse storeResponse = cartingAndOrderProductService.removeProductFromCart(1, 102);
        assertEquals("Product has been removed from cart", storeResponse.getMessage());
    }

    @Test
    void testThatCustomerCanOrderProduct() {
        OrderProductRequest orderProductRequest = new OrderProductRequest();
        orderProductRequest.setDeliveryAddress("312, Herbert Macualay, Yaba");
        StoreResponse response = cartingAndOrderProductService.orderProduct(1, orderProductRequest);
        assertEquals("Kindly proceed to payment to make your order successful", response.getMessage());
    }

}