package com.starlightonlinestore.Data.Models;

import jakarta.persistence.*;
import lombok.*;


import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Vendor extends AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String storeName;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> storeAddress = new HashSet<>();
}
