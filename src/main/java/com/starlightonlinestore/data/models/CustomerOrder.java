package com.starlightonlinestore.data.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;
    private Integer itemTotal;
    private String deliveryAddress;
    private double total;
    private LocalDateTime localDateTime;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
}
