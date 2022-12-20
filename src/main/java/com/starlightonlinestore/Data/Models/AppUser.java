package com.starlightonlinestore.Data.Models;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;



@Data
@MappedSuperclass
public class AppUser {
    private String email;
    private String password;
    private String phoneNumber;

}
