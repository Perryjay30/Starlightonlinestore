package com.starlightonlinestore.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


public enum Role {
    SUPER_ADMIN, ADMIN, USER, VENDOR
}
