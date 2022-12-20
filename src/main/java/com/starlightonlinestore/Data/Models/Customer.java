package com.starlightonlinestore.Data.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Builder
public class Customer extends AppUser  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String firstName;
    private String LastName;
    private boolean isActive;

    @ElementCollection
    private Set<String> deliveryAddress = new HashSet<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> orders = new ArrayList<>();
}
