package com.starlightonlinestore.service;

import com.starlightonlinestore.data.exceptions.CustomerRegistrationException;
import com.starlightonlinestore.data.models.Customer;
import com.starlightonlinestore.data.models.CustomerOrder;
import com.starlightonlinestore.data.models.OTPToken;
import com.starlightonlinestore.data.repository.CustomerRepository;
import com.starlightonlinestore.data.repository.CustomerOrderRepository;
import com.starlightonlinestore.data.dto.Request.*;
import com.starlightonlinestore.data.dto.Response.*;
import com.starlightonlinestore.data.repository.OtpTokenRepository;
import com.starlightonlinestore.utils.validators.EmailService;
import com.starlightonlinestore.utils.validators.Token;
import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.starlightonlinestore.data.models.Role.CUSTOMER;
import static com.starlightonlinestore.data.models.Status.UNVERIFIED;
import static com.starlightonlinestore.data.models.Status.VERIFIED;


@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerOrderRepository orderRepository;

    private final OtpTokenRepository otpTokenRepository;

    private final EmailService emailService;

    private final CustomerOrderRepository customerOrderRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerOrderRepository orderRepository, OtpTokenRepository otpTokenRepository, EmailService emailService, CustomerOrderRepository customerOrderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.otpTokenRepository = otpTokenRepository;
        this.emailService = emailService;
        this.customerOrderRepository = customerOrderRepository;
    }


    @Override
    public String register(CustomerRegistrationRequest registrationRequest) {
        if(!UserDetailsValidator.isValidEmailAddress(registrationRequest.getEmail()))
            throw new CustomerRegistrationException(String.
                    format("email %s is invalid", registrationRequest.getEmail()));
        Customer customer = buildBuyer(registrationRequest);
        customerRepository.save(customer);
        SendOtpRequest OTPRequest = new SendOtpRequest();
        OTPRequest.setEmail(registrationRequest.getEmail());
        return sendOTP(OTPRequest);
    }

    @Override
    public CustomerRegistrationResponse createAccount(VerifyOtpRequest verifyOtpRequest) {
        log.info(verifyOtpRequest.getEmail());
        log.info(verifyOtpRequest.getToken());
        verifyOTP(verifyOtpRequest);
        var savedCustomer = customerRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer does not exists"));
//        savedCustomer.setStatus(VERIFIED);
//        customerRepository.save(savedCustomer);
        customerRepository.enableCustomer(VERIFIED, savedCustomer.getEmail());
        return buildBuyerRegistrationResponse(savedCustomer);
    }

    @Override
    public void verifyOTP(VerifyOtpRequest verifyOtpRequest) {
        OTPToken foundToken = otpTokenRepository.findByToken(verifyOtpRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(!Objects.equals(verifyOtpRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), verifyOtpRequest.getToken());
//        var token = otpTokenRepository.findByToken(foundToken.getToken()).orElseThrow(()->new RuntimeException("token not found"));
//        token.setVerifiedAt(LocalDateTime.now());
//        otpTokenRepository.save(token);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws MessagingException {
        Customer forgotCustomer = customerRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("This email is not registered"));
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), forgotCustomer);
        otpTokenRepository.save(otpToken);
        emailService.sendEmail(forgotPasswordRequest.getEmail(), forgotCustomer.getFirstName(), token);
        return "Token successfully sent to your email. Please check.";
//        SendOtpRequest request = new SendOtpRequest();
//        request.setEmail(forgotPasswordRequest.getEmail());
//        return generateOtpToken(request, forgotCustomer);
    }

    @Override
    public Response resetPassword(ResetPasswordRequest resetPasswordRequest) {
        verifyOtpForResetPassword(resetPasswordRequest);
        Customer foundCustomer = customerRepository.findByEmail(resetPasswordRequest.getEmail()).get();
        foundCustomer.setPassword(resetPasswordRequest.getPassword());
        if(BCrypt.checkpw(resetPasswordRequest.getConfirmPassword(), resetPasswordRequest.getPassword())) {
            customerRepository.save(foundCustomer);
            return new Response("Your password has been reset successfully");
        } else {
            throw new IllegalStateException("Password does not match");
        }
    }

    private void verifyOtpForResetPassword(ResetPasswordRequest resetPasswordRequest) {
        var foundToken = otpTokenRepository.findByToken(resetPasswordRequest.getToken())
                .orElseThrow(() -> new RuntimeException("Token doesn't exist"));
        if(foundToken.getVerifiedAt() != null)
            throw new RuntimeException("Token has been used");
        if(foundToken.getExpiredAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Token has expired");
        if(!Objects.equals(resetPasswordRequest.getToken(), foundToken.getToken()))
            throw new RuntimeException("Incorrect token");
        otpTokenRepository.setVerifiedAt(LocalDateTime.now(), resetPasswordRequest.getToken());
    }

    @Override
    public String sendOTP(SendOtpRequest sendOtpRequest) {
        Customer savedCustomer = customerRepository.findByEmail(sendOtpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));
        return generateOtpToken(sendOtpRequest, savedCustomer);
    }

    private String generateOtpToken(SendOtpRequest sendOtpRequest, Customer savedCustomer) {
        String token = Token.generateToken(4);
        OTPToken otpToken = new OTPToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), savedCustomer);
        otpTokenRepository.save(otpToken);
        emailService.send(sendOtpRequest.getEmail(), emailService.buildEmail(savedCustomer.getFirstName(), token));
        return "Token successfully sent to your email. Please check.";

    }
    private CustomerRegistrationResponse buildBuyerRegistrationResponse(Customer savedCustomer) {
        CustomerRegistrationResponse response = new CustomerRegistrationResponse();
        response.setMessage("User registration successful");
        response.setStatusCode(201);
        response.setUserId(savedCustomer.getId());
        return response;
    }

    private Customer buildBuyer(CustomerRegistrationRequest registrationRequest) {
        Customer customer = new Customer();
        customer.setFirstName(registrationRequest.getFirstName());
        customer.setLastName(registrationRequest.getLastName());
        if(customerRepository.findByEmail(registrationRequest.getEmail()).isPresent())
            throw new RuntimeException("This email has been taken, kindly register with another email address");
        else
            customer.setEmail(registrationRequest.getEmail());
        customer.setPassword(registrationRequest.getPassword());
        customer.setStatus(UNVERIFIED);
        customer.setRole(CUSTOMER);
        return customer;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Customer foundCustomer = customerRepository.findByEmail(loginRequest
                .getEmail()).orElseThrow(() -> new RuntimeException("your email is incorrect"));
        if(foundCustomer.getStatus() != VERIFIED) throw new RuntimeException("Customer is not verified");
//      foundCustomer.setEmail(loginRequest.getEmail())
        LoginResponse loginResponse = new LoginResponse();
//        if(foundCustomer.getPassword().equals(loginRequest.getPassword())) {
        if(BCrypt.checkpw(loginRequest.getPassword(), foundCustomer.getPassword())) {
            loginResponse.setMessage("successful login");
            return loginResponse;
        }
        loginResponse.setMessage("authentication failed");
        return loginResponse;
    }

    @Override
    public Response changePassword(ChangePasswordRequest changePasswordRequest) {
        Customer verifiedCustomer = customerRepository.findByEmail(changePasswordRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("customer isn't registered"));
        if(BCrypt.checkpw(changePasswordRequest.getOldPassword(), verifiedCustomer.getPassword()))
            verifiedCustomer.setPassword(changePasswordRequest.getNewPassword());
        customerRepository.save(verifiedCustomer);
        return new Response("Your password has been successfully changed");
    }

    @Override
    public OrderProductResponse orderProduct(Integer id, OrderProductRequest orderProductRequest) {
        Customer customer =
                customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Kindly enter a valid customer Id"));
        CustomerOrder order = placingOrder(orderProductRequest);
        customer.getCustomerOrderList().add(order);
        CustomerOrder orderedProduct = orderRepository.save(order);
        return placedOrderResponse(orderedProduct);
    }
//        if(product.getQuantity() >= orderProductRequest.getQuantity()) {


//        }
//        else
//            throw new RuntimeException("order quantity larger than available quantity");



//            customerRepository.save(customer);

    private OrderProductResponse placedOrderResponse(CustomerOrder orderedProduct) {
        OrderProductResponse orderProductResponse = new OrderProductResponse();
        orderProductResponse.setId(orderedProduct.getId());
        orderProductResponse.setStatusCode(201);
        orderProductResponse.setMessage("You just made a successful order");
        return orderProductResponse;
    }

    private CustomerOrder placingOrder(OrderProductRequest orderProductRequest) {
        CustomerOrder order = new CustomerOrder();
        order.setProductName(orderProductRequest.getProductName());
        order.setProductCategory(orderProductRequest.getProductCategory());
        order.setPrice(orderProductRequest.getPrice());
        order.setQuantity(orderProductRequest.getQuantity());
        order.setTotal(order.getPrice() * order.getQuantity());
        return order;
    }

    @Override
    public Response deleteCustomer(int id, DeleteRequest deleteRequest) {
        Customer foundCustomer = customerRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Customer doesn't exist"));
        String randomToken = UUID.randomUUID().toString();
        String encoded = BCrypt.hashpw(randomToken, BCrypt.gensalt());
        if (BCrypt.checkpw(deleteRequest.getPassword(), foundCustomer.getPassword())) {
            String deleteCustomer = "Deleted" + " " + foundCustomer.getEmail() + " " + encoded;
            foundCustomer.setEmail(deleteCustomer);
            customerRepository.save(foundCustomer);
            return new Response("Customer deleted");
        } else {
            throw new RuntimeException("Customer can't be deleted");
        }
    }

    @Override
    public List<CustomerOrder> getAllOrders() {
        return customerOrderRepository.findAll();
    }

    @Override
    public Response updateCustomer(Integer id, UpdateRequest updateRequest) {
        var customer = customerRepository.findById(id);
        if (customer.isEmpty()) throw new RuntimeException("customer not found");
        Customer foundCustomer = updatingTheCustomer(updateRequest, customer);
        customerRepository.save(foundCustomer);
        return new Response("Customer updated successfully");
    }

    private Customer updatingTheCustomer(UpdateRequest updateRequest, Optional<Customer> customer) {
        var foundCustomer = customer.get();
        foundCustomer.setFirstName(updateRequest.getFirstName());
        if(customerRepository.findByPhoneNumber(updateRequest.getPhone()).isPresent())
            throw new RuntimeException("This Phone Number has been taken, kindly register with another");
        else
            foundCustomer.setPhoneNumber(updateRequest.getPhone()!= null && !updateRequest.getPhone()
                .equals("") ? updateRequest.getPhone() : foundCustomer.getPhoneNumber());
        return stillUpdatingCustomer(updateRequest, foundCustomer);
    }

    private Customer stillUpdatingCustomer(UpdateRequest updateRequest, Customer foundCustomer) {
        foundCustomer.setLastName(updateRequest.getLastName());
        Set<String> buyersAddressList = foundCustomer.getDeliveryAddress();
        buyersAddressList.add(updateRequest.getDeliveryAddress());
        foundCustomer.setEmail(updateRequest.getEmail() != null && !updateRequest.getEmail()
                .equals("") ? updateRequest.getEmail() : foundCustomer.getEmail());
        return foundCustomer;
    }


}
