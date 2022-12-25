package com.starlightonlinestore.Service;

import com.starlightonlinestore.Data.Exceptions.CustomerRegistrationException;
import com.starlightonlinestore.Data.Models.Customer;
import com.starlightonlinestore.Data.Models.CustomerOrder;
import com.starlightonlinestore.Data.Repository.CustomerRepository;
import com.starlightonlinestore.Data.Repository.OrderRepository;
import com.starlightonlinestore.Data.dto.Request.*;
import com.starlightonlinestore.Data.dto.Response.*;
import com.starlightonlinestore.utils.validators.UserDetailsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public CustomerRegistrationResponse register(CustomerRegistrationRequest registrationRequest) {
        if(!UserDetailsValidator.isValidEmailAddress(registrationRequest.getEmail()))
            throw new CustomerRegistrationException(String.
                    format("email %s is invalid", registrationRequest.getEmail()));

        if(!UserDetailsValidator.isValidPassword(registrationRequest.getPassword()))
            throw new CustomerRegistrationException(String.
                    format("password %s is too weak", registrationRequest.getPassword()));

        if(!UserDetailsValidator.isValidPhoneNumber(registrationRequest.getPhoneNumber()))
            throw new CustomerRegistrationException(String.
                    format("PhoneNumber %s is invalid", registrationRequest.getPhoneNumber()));

        Customer customer = buildBuyer(registrationRequest);

        Customer savedCustomer = customerRepository.save(customer);

        return buildBuyerRegistrationResponse(savedCustomer);

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
        customer.setEmail(registrationRequest.getEmail());
        customer.setPassword(registrationRequest.getPassword());
        Set<String> buyersAddressList = customer.getDeliveryAddress();
        buyersAddressList.add(registrationRequest.getAddress());
        customer.setPhoneNumber(registrationRequest.getPhoneNumber());
        return customer;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Customer foundCustomer = customerRepository.findByEmail(loginRequest
                .getEmail()).orElseThrow(() -> new RuntimeException("your email is incorrect"));

        LoginResponse loginResponse = new LoginResponse();
        if(foundCustomer.getPassword().equals(loginRequest.getPassword())) {
            loginResponse.setMessage("successful login");
            return loginResponse;
        }
        loginResponse.setMessage("authentication failed");
        return loginResponse;
    }

    @Override
    public OrderProductResponse orderProduct(OrderProductRequest orderProductRequest) {
        Customer customer =
                customerRepository.findById(orderProductRequest.
                        getCustomerId()).orElseThrow(() -> new RuntimeException("Kindly enter a valid customer Id"));
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
    public Response deleteCustomer(int id) {
        customerRepository.deleteById(id);
        return new Response("Customer deleted");
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    @Override
    public Response updateCustomer(UpdateRequest updateRequest) {
        var customer = customerRepository.findById(updateRequest.getId());
        if (customer.isEmpty()) throw new RuntimeException("customer not found");
        Customer foundCustomer = updatingTheCustomer(updateRequest, customer);
        customerRepository.save(foundCustomer);
        return new Response("Customer updated successfully");
    }

    private Customer updatingTheCustomer(UpdateRequest updateRequest, Optional<Customer> customer) {
        var foundCustomer = customer.get();
        foundCustomer.setFirstName(updateRequest.getFirstName());
        foundCustomer.setLastName(updateRequest.getLastName());
        foundCustomer.setPhoneNumber(updateRequest.getPhone()!= null && !updateRequest.getPhone()
                .equals("") ? updateRequest.getPhone() : foundCustomer.getPhoneNumber());
        foundCustomer.setEmail(updateRequest.getEmail() != null && !updateRequest.getEmail()
                .equals("") ? updateRequest.getEmail() : foundCustomer.getEmail());
        foundCustomer.setPassword(updateRequest.getPassword() != null && !updateRequest.getPassword()
                .equals("") ? updateRequest.getPassword() : foundCustomer.getPassword());
        return foundCustomer;
    }
}
