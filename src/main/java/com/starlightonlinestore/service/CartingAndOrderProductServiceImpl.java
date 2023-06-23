package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.AddToCartRequest;
import com.starlightonlinestore.data.dto.Request.OrderProductRequest;
import com.starlightonlinestore.data.dto.Request.PaymentRequest;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import com.starlightonlinestore.data.exceptions.StoreException;
import com.starlightonlinestore.data.models.*;
import com.starlightonlinestore.data.repository.CartRepository;
import com.starlightonlinestore.data.repository.CustomerOrderRepository;
import com.starlightonlinestore.data.repository.CustomerRepository;
import com.starlightonlinestore.utils.validators.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartingAndOrderProductServiceImpl implements CartingAndOrderProductService{

    private final CustomerOrderRepository customerOrderRepository;
    private  final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;

    private final CustomerServiceImpl customerService;

    @Override
    public StoreResponse addProductToCart(Integer id, AddToCartRequest addToCartRequest) {
        Customer customer = customerService.getFoundCustomer(customerRepository.findById(id),
                "Customer doesn't exist");
        Cart cart = AddingItemsToCart(addToCartRequest);
        customer.getCustomerCart().add(cart);
        cartRepository.save(cart);
        return new StoreResponse("Product successfully added to cart");
    }

    @Override
    public StoreResponse removeProductFromCart(Integer customerId, Integer productInCartId) {
        customerService.getFoundCustomer(customerRepository.findById(customerId),
                "Customer doesn't exist");
        cartRepository.deleteById(productInCartId);
        return new StoreResponse("Product has been removed from cart");
    }

    public List<Cart> getAllCart() {
        return cartRepository.findAll();
    }
//        if(product.getQuantity() >= orderProductRequest.getQuantity()) {


//        }
//        else
//            throw new RuntimeException("order quantity larger than available quantity");



//            customerRepository.save(customer);

    private Cart AddingItemsToCart(AddToCartRequest addToCartRequest) {
        Cart newCart = new Cart();
        newCart.setProductName(addToCartRequest.getProductName());
        newCart.setProductCategory(addToCartRequest.getProductCategory());
        newCart.setPrice(addToCartRequest.getPrice());
        newCart.setQuantity(addToCartRequest.getQuantity());
        newCart.setLocalDateTime(LocalDateTime.now());
//        order.setTotal(order.getPrice() * order.getQuantity());
        return newCart;
    }

    @Override
    public List<CustomerOrder> getAllOrders() {
        return customerOrderRepository.findAll();
    }


    @Override
    public StoreResponse CustomerCanMakePaymentForGoodsOrdered(Integer customerId, Integer orderId, PaymentRequest paymentRequest) throws IOException, MessagingException {
        Customer existingCustomer = customerService.getFoundCustomer(customerRepository.findById(customerId),
                "Customer doesn't exist");
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new StoreException("Goods wasn't ordered"));
        paymentService.makePaymentForGoods(paymentRequest);
        customerOrder.setOrderStatus(OrderStatus.ORDER_SUCCESSFUL);
        customerOrder.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
        cartRepository.deleteAll();
        emailService.sendEmailForSuccessfulOrder(existingCustomer.getEmail(), existingCustomer.getFirstName(), customerOrder.getOrderId());
        return new StoreResponse("Order placed, Check your email for notification");
    }

    @Override
    public StoreResponse orderProduct(Integer id, OrderProductRequest orderProductRequest) {
        Customer customer = customerService.getFoundCustomer(customerRepository.findById(id),
                "Kindly enter a valid customer id");
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setDeliveryAddress(orderProductRequest.getDeliveryAddress());
        customer.getDeliveryAddress().add(customerOrder.getDeliveryAddress());
        customerOrder.setLocalDateTime(LocalDateTime.now());
        customerOrder.setOrderStatus(OrderStatus.PENDING);
        customerOrder.setPaymentStatus(PaymentStatus.PROCESSING);
        totalQuantityOfProductInCart(customerOrder);
        totalAmountOfAllProductInCart(customerOrder);
        customerOrderRepository.save(customerOrder);
        return new StoreResponse("Kindly proceed to payment to make your order successful");
    }

    private void totalQuantityOfProductInCart(CustomerOrder customerOrder) {
        List<Integer> quantityCart = new ArrayList<>();
        List<Cart> allCart = getAllCart();
        int sum = 0;
        for (Cart myCart : allCart) {
            if(myCart.getQuantity() > 0) quantityCart.add(myCart.getQuantity());
        }
        for (int i = 0; i < quantityCart.size(); i++) {
            sum += quantityCart.get(i);
        }
        customerOrder.setItemTotal(sum);
    }

    private void totalAmountOfAllProductInCart(CustomerOrder customerOrder) {
        List<Double> totalAmountCart = new ArrayList<>();
        List<Cart> allCart = getAllCart();
        double total = 0;
        for (Cart amountCart : allCart) {
            if(amountCart.getPrice() > 0) totalAmountCart.add(amountCart.getPrice());
        }
        for(int j = 0; j < totalAmountCart.size(); j++) {
            total += totalAmountCart.get(j);
        }
        customerOrder.setTotal(total);
    }
}
