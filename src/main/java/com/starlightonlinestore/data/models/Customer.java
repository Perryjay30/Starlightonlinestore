package com.starlightonlinestore.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@Data
@Entity
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String LastName;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> deliveryAddress = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomerOrder> customerOrderList = new ArrayList<>();
}
