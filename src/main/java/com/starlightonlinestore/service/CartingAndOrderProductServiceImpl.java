package com.starlightonlinestore.service;

import com.starlightonlinestore.data.dto.Request.AddToCartRequest;
import com.starlightonlinestore.data.dto.Request.OrderProductRequest;
import com.starlightonlinestore.data.dto.Request.PaymentRequest;
import com.starlightonlinestore.data.dto.Response.StoreResponse;
import com.starlightonlinestore.data.exceptions.StoreException;
import com.starlightonlinestore.data.models.*;
import com.starlightonlinestore.data.repository.CartRepository;
import com.starlightonlinestore.data.repository.CustomerOrderRepository;
import com.starlightonlinestore.data.repository.UserRepository;
import com.starlightonlinestore.data.repository.ProductRepository;
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
public class CartingAndOrderProductServiceImpl implements CartingAndOrderProductService {

    private final CustomerOrderRepository customerOrderRepository;
    private  final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PaymentService paymentService;
    private final EmailService emailService;
    private final UserService userService;
    private final ProductRepository productRepository;

    @Override
    public StoreResponse addProductToCart(Integer customerId, AddToCartRequest addToCartRequest) {
        User user = userService.getFoundCustomer(userRepository.findById(customerId),
                "Customer doesn't exist");
        Cart cart = AddingItemsToCart(addToCartRequest);
        user.getCustomerCart().add(cart);
        cartRepository.save(cart);
        return new StoreResponse("Product successfully added to cart");
    }

    @Override
    public StoreResponse removeProductFromCart(Integer customerId, Integer productInCartId) {
        userService.getFoundCustomer(userRepository.findById(customerId),
                "Customer doesn't exist");
        cartRepository.deleteById(productInCartId);
        return new StoreResponse("Product has been removed from cart");
    }

    public List<Cart> getCart() {
        return cartRepository.findAll();
    }

    public void removeProduct() {
        List<Cart> theCart = getCart();
        for (Cart cart : theCart) {
//            if(cart.getProductId() == )
        }
    }

    private Cart AddingItemsToCart(AddToCartRequest addToCartRequest) {
        Product availableProduct = productRepository.findById(addToCartRequest.getProductId())
                .orElseThrow(() -> new StoreException("Product isn't available"));
        Cart newCart = new Cart();
        newCart.setProductName(availableProduct.getProductName());
        newCart.setProductCategory(availableProduct.getCategory());
        newCart.setUnitPrice(availableProduct.getUnitPrice());
        return AddingItemsToCart2(addToCartRequest, availableProduct, newCart);
    }

    private Cart AddingItemsToCart2(AddToCartRequest addToCartRequest, Product availableProduct, Cart newCart) {
        if(availableProduct.getQuantity() >= addToCartRequest.getQuantity())
            newCart.setQuantity(addToCartRequest.getQuantity());
            else throw new StoreException("order quantity larger than available quantity");
//        int i = newCart.getQuantity() - availableProduct.getQuantity();
        newCart.setTotalPrice(availableProduct.getUnitPrice() * addToCartRequest.getQuantity());
        newCart.setLocalDateTime(LocalDateTime.now());
        return newCart;
    }

    @Override
    public List<CustomerOrder> getAllOrders(Integer customerId) {
        userService.getFoundCustomer(userRepository.findById(customerId),
                "Customer doesn't exist");
        return customerOrderRepository.findAll();
    }


    @Override
    public StoreResponse CustomerCanMakePaymentForGoodsOrdered(Integer customerId, Integer orderId, PaymentRequest paymentRequest) throws IOException, MessagingException {
        User existingUser = userService.getFoundCustomer(userRepository.findById(customerId),
                "User doesn't exist");
        CustomerOrder customerOrder = customerOrderRepository.findById(orderId)
                .orElseThrow(() -> new StoreException("Goods wasn't ordered"));
        paymentService.makePaymentForGoods(paymentRequest);
        customerOrder.setOrderStatus(OrderStatus.ORDER_SUCCESSFUL);
        customerOrder.setPaymentStatus(PaymentStatus.PAYMENT_SUCCESSFUL);
        cartRepository.deleteAll();
        emailService.sendEmailForSuccessfulOrder(existingUser.getEmail(), existingUser.getFirstName(), customerOrder.getOrderId());
        return new StoreResponse("Order placed, Check your email for notification");
    }

    @Override
    public StoreResponse orderProduct(Integer customerId, OrderProductRequest orderProductRequest) {
        User user = userService.getFoundCustomer(userRepository.findById(customerId),
                "Kindly enter a valid customer id");
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setDeliveryAddress(orderProductRequest.getDeliveryAddress());
        user.getDeliveryAddress().add(customerOrder.getDeliveryAddress());
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
        List<Cart> allCart = getCart();
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
        List<Cart> allCart = getCart();
        double total = 0;
        for (Cart amountCart : allCart) {
            if(amountCart.getTotalPrice() > 0) totalAmountCart.add(amountCart.getTotalPrice());
        }
        for(int j = 0; j < totalAmountCart.size(); j++) {
            total += totalAmountCart.get(j);
        }
        customerOrder.setTotal(total);
    }
}
