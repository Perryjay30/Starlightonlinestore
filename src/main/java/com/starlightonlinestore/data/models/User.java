package com.starlightonlinestore.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String email;
    private String password;
    private String phoneNumber;
    private String storeName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> deliveryAddress = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomerOrder> customerOrderList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Cart> customerCart = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> storeAddress = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();
}
