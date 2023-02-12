package com.starlightonlinestore.data.models;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String email;
    private String password;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String storeName;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> storeAddress = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();
}
